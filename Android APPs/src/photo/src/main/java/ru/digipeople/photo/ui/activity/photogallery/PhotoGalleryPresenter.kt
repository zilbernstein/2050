package ru.digipeople.photo.ui.activity.photogallery

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.photo.helper.FileInfo
import ru.digipeople.photo.helper.PhotoFileStorage
import ru.digipeople.photo.model.Photo
import ru.digipeople.photo.ui.activity.photogallery.interactor.PhotoLoader
import ru.digipeople.photo.ui.activity.photogallery.interactor.PhotoUploader
import ru.digipeople.photo.ui.activity.photogallery.interactor.PhotoWorker
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер фотогалереии
 *
 * @author Kashonkov Nikita
 */
//ToDo перенести логики подгрузки и отображения фотографий на glide/picasso
class PhotoGalleryPresenter @Inject constructor(val context: Context,
                                                viewState: PhotoGalleryViewState,
                                                private val id: String,
                                                private val startType: PhotoGalleryStartMode,
                                                private val photoUploader: PhotoUploader,
                                                private val photoStorage: PhotoFileStorage,
                                                private val photoLoader: PhotoLoader,
                                                private val photoWorker: PhotoWorker)
    : BaseMvpViewStatePresenter<PhotoGalleryView, PhotoGalleryViewState>(viewState) {
    //region Const
    private val MAX_PHOTO_COUNT = 5
    //end Region
    //region Other
    var photoList = mutableListOf<Photo>()
    var currentPhoto: Photo? = null
    lateinit var currentFileInfo: FileInfo
    val logger = LoggerFactory.getLogger(PhotoGalleryPresenter::class)
    var loadDisposables = Disposables.disposed()
    var uploadDisposable = Disposables.disposed()
    var deleteDisposable = Disposables.disposed()
    var fileDisposables = Disposables.disposed()
    //endRegion

    override fun onInitialize() {
        view.setUpMode(startType)
        loadPhotos()
    }
    /**
     * Обработка удаления фото
     */
    fun onPhotoDeleteClicked(photo: Photo) {
        deletePhoto(photo)
    }
    /**
     * Обрабокта нажатия на фото
     */
    fun onPhotoClicked(photo: Photo) {
        currentPhoto = photo
        view.setPhoto(photo)
    }
    /**
     * Обрабьотка создания фото
     */
    fun onCreatePhotoClicked() {
        view.checkPermissions()
    }
    /**
     * Нажатие назад
     */
    fun onBackClicked() {
        view.navigateBack()
    }
    /**
     * Обработка подтверждения
     */
    fun onCheckClicked() {
        uploadPhoto()
    }
    /**
     * разрешения даны
     */
    fun permissionsGranted() {
        fileDisposables.dispose()
        fileDisposables = Single.fromCallable { photoStorage.createFile() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { info ->
                    currentFileInfo = info
                    view.navigateToCreatePhoto(info.uri)
                }
    }
    /**
     * Фотографии сделаны
     */
    fun photoWasMade() {
        if (photoStorage.checkFileIsEmpty(currentFileInfo.uri)) {
            photoStorage.deleteFile(currentFileInfo.uri)
            return
        }

        val photo = Photo().apply {
            url = currentFileInfo.uri.toString()
            id = currentFileInfo.name
            isNew = true
        }

        photoList.add(photo)

        view.changeCreatePhotoButtonVisability(photoList.size < MAX_PHOTO_COUNT)
        view.setDataToAdapter(photoList)
        view.scrollRecycler(photoList.size - 1)
        view.setPhoto(photo)
    }
    /**
     * Ошибка при фотографии
     */
    fun photoMistake() {
        if (photoStorage.checkFileIsEmpty(currentFileInfo.uri)) {
            photoStorage.deleteFile(currentFileInfo.uri)
            return
        }
    }

    override fun destroy() {
        uploadDisposable.dispose()
        deleteDisposable.dispose()
        fileDisposables.dispose()
        fileDisposables = Completable.fromAction { photoStorage.deleteFiles() }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { super.destroy() }
    }
    /**
     * Загрузка фото
     */
    private fun loadPhotos() {
        lateinit var loadSingle: Single<PhotoLoader.Result>
        /**
         * Разные методы в завимисимости от места вызова
         */
        when (startType) {
            PhotoGalleryStartMode.PHOTO_MODE_WORK, PhotoGalleryStartMode.VIEW_MODE_WORK -> loadSingle = photoLoader.loadWorkPhotos(id)
            PhotoGalleryStartMode.PHOTO_MODE_REMARK, PhotoGalleryStartMode.VIEW_MODE_REMARK -> loadSingle = photoLoader.loadRemarkPhotos(id)
            PhotoGalleryStartMode.PHOTO_MODE_CSO, PhotoGalleryStartMode.VIEW_MODE_CSO -> loadSingle = photoLoader.loadCsoPhotos(id)
        }
        loadDisposables = loadSingle
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisability(true) }
                .doOnSuccess { result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        photoList.addAll(result.photos!!)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisability(false)
                    if (result.isSuccessful) {
                        view.setDataToAdapter(photoList)
                        view.setPhoto(photoList.lastOrNull())
                        if (startType == PhotoGalleryStartMode.PHOTO_MODE_REMARK||startType == PhotoGalleryStartMode.PHOTO_MODE_WORK || startType == PhotoGalleryStartMode.PHOTO_MODE_CSO){
                            view.changeCreatePhotoButtonVisability(photoList.size < MAX_PHOTO_COUNT)
                        }
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })

    }
    /**
     * Отправка фото
     */
    private fun uploadPhoto() {
        val newPhotoList = photoList.filter { it.isNew }
        if (newPhotoList.isNotEmpty()) {

            var sentPhotoCount = 0
            var sentIds = mutableListOf<String>()
            lateinit var sendingPhoto: Photo
            /**
             * отправка 1 фото
             */
            var uploadSingle = Single.fromCallable { sendingPhoto = newPhotoList.first() }
                    .flatMap { photoUploader.uploadPhoto(sendingPhoto.id) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {
                        view.setSendingProgress(++sentPhotoCount, newPhotoList.size)
                        sentIds.add(sendingPhoto.id)
                    }
            /**
             * Отпарвка больше 1
             */
            if (newPhotoList.size > 1) {
                for (i in 1 until newPhotoList.size) {
                    val photo = newPhotoList[i]
                    uploadSingle = uploadSingle
                            .observeOn(Schedulers.io())
                            .map { sendingPhoto = photo }
                            .flatMap { photoUploader.uploadPhoto(photo.id) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSuccess {
                                view.setSendingProgress(++sentPhotoCount, newPhotoList.size)
                                sentIds.add(sendingPhoto.id)
                            }
                }
            }
            uploadDisposable = uploadSingle.observeOn(Schedulers.io())
                    .flatMap {
                        lateinit var clipSingle: Single<PhotoWorker.Result>
                        /**
                         * разные методы в зависимости о места вызова
                         */
                        when (startType) {
                            PhotoGalleryStartMode.PHOTO_MODE_WORK -> clipSingle = photoWorker.clipPhotosToWork(id, sentIds)
                            PhotoGalleryStartMode.PHOTO_MODE_REMARK -> clipSingle = photoWorker.clipPhotosToRemark(id, sentIds)
                            PhotoGalleryStartMode.PHOTO_MODE_CSO -> clipSingle = photoWorker.clipPhotosToCso(id, sentIds)
                        }
                        clipSingle
                    }
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        view.setProgressVisibility(true)
                        view.setSendingProgress(sentPhotoCount, newPhotoList.size)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        /**
                         * Обработка результата
                         */
                        if (result.isSuccessful) {
                            view.setProgressVisibility(false)
                            view.navigateOnCheckButton()
                        } else {
                            /**
                             * отображение ошибки
                             */
                            view.setProgressVisibility(false)
                            view.showError(result.userError.message)
                        }
                    }, {
                        view.showError(it.message!!)
                    })

        } else {
            view.navigateOnCheckButton()
        }
    }
    /**
     * Удаление фото
     */
    private fun deletePhoto(photo: Photo) {
        if (photo.isNew) {
            deletePhotoFromList(photo)
        } else {
            lateinit var deleteSingle: Single<PhotoWorker.Result>
            /**
             * Разные методы в зависимости от места вызова
             */
            when (startType) {
                PhotoGalleryStartMode.PHOTO_MODE_WORK -> deleteSingle = photoWorker.deletePhotoFromWork(id, photo.id)
                PhotoGalleryStartMode.PHOTO_MODE_REMARK -> deleteSingle = photoWorker.deletePhotoFromRemark(id, photo.id)
                PhotoGalleryStartMode.PHOTO_MODE_CSO -> deleteSingle = photoWorker.deletePhotoFromCso(id, photo.id)
            }

            deleteDisposable = deleteSingle
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { view.setLoadingVisability(true) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        /**
                         * Обрабока результата
                         */
                        view.setLoadingVisability(false)
                        if (result.isSuccessful) {
                            deletePhotoFromList(photo)
                        } else {
                            /**
                             * Отображение ошибки
                             */
                            view.showError(result.userError.message)
                        }
                    }, {
                        view.setLoadingVisability(false)
                        view.showError(it.message!!)
                    })
        }
    }
    /**
     * Удаление фото из списка
     */
    private fun deletePhotoFromList(photo: Photo) {
        photoList.remove(photo)
        view.changeCreatePhotoButtonVisability(photoList.size < MAX_PHOTO_COUNT)
        view.setDataToAdapter(photoList)
        if (currentPhoto == photo && photoList.isNotEmpty()) {
            view.setPhoto(photoList.last())
        } else if (photoList.isEmpty()) {
            view.setPhoto(null)
        }
    }
}