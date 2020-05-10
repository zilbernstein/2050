package ru.digipeople.photo.ui.activity.photogallery

import android.net.Uri
import ru.digipeople.photo.model.Photo
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import java.text.FieldPosition
import javax.inject.Inject

/**
 * ViewState для фотогалереи
 *
 * @author Kashonkov Nikita
 */
class PhotoGalleryViewState @Inject constructor() : BaseMvpViewState<PhotoGalleryView>(), PhotoGalleryView {
    //regionOther
    var isFirstAttached = true
    var photoList: List<Photo> = emptyList()
    var currentPhoto: Photo? = null
    var isPhotoButtonVisible = true
    var isProgressVisible = false
    var isLoadingVisible = false
    var currentLoadingProgress = -1
    var maxLoadingProgress = -1
    var isPhotosUploaded = false
    var position = 0
    var startMode: PhotoGalleryStartMode? = null
    //endRegion

    override fun onViewAttached(view: PhotoGalleryView) {
        if (isFirstAttached) {
            view.setUpMode(startMode!!)

            if (startMode == PhotoGalleryStartMode.VIEW_MODE_WORK || startMode == PhotoGalleryStartMode.VIEW_MODE_REMARK) {
                isPhotoButtonVisible = false
            }

            isFirstAttached = false
        }

        view.scrollRecycler(position)
        view.setDataToAdapter(photoList)
        view.setPhoto(currentPhoto)

        if (isPhotosUploaded) {
            view.navigateOnCheckButton()
        }
        /**
         * Управление видимостью прогресса
         */
        view.setProgressVisibility(isProgressVisible)

        if (isProgressVisible) {
            view.setSendingProgress(currentLoadingProgress, maxLoadingProgress)
        }
        /**
         * Управление видимостью кнопки
         */
        view.changeCreatePhotoButtonVisability(isPhotoButtonVisible)
        /**
         * Управление видимостью лодера
         */
        view.setLoadingVisability(isLoadingVisible)
    }

    override fun onViewDetached(view: PhotoGalleryView?) {}
    /**
     * установка фото
     */
    override fun setPhoto(photo: Photo?) {
        currentPhoto = photo
        forEachView { it.setPhoto(photo) }
    }
    /**
     * переход к созданию фото
     */
    override fun navigateToCreatePhoto(uri: Uri) {
        forEachView { it.navigateToCreatePhoto(uri) }
    }
    /**
     * установка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Photo>) {
        photoList = list
        forEachView { it.setDataToAdapter(list) }
    }
    /**
     * управление видимостью кнопки
     */
    override fun changeCreatePhotoButtonVisability(isVisible: Boolean) {
        isPhotoButtonVisible = isVisible
        forEachView { it.changeCreatePhotoButtonVisability(isVisible) }
    }
    /**
     * переход назад
     */
    override fun navigateBack() {
        forEachView { it.navigateBack() }
    }

    override fun setUpMode(startType: PhotoGalleryStartMode) {
        startMode = startType
    }

    override fun navigateOnCheckButton() {
        isPhotosUploaded = true
        forEachView { it.navigateOnCheckButton() }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * Проверка разрешений
     */
    override fun checkPermissions() {
        forEachView { it.checkPermissions() }
    }
    /**
     * Установка прогресса
     */
    override fun setSendingProgress(currentProgress: Int, maxProgress: Int) {
        currentLoadingProgress = currentProgress
        maxLoadingProgress = maxProgress
        forEachView { it.setSendingProgress(currentProgress, maxProgress) }
    }
    /**
     * Управление видимостью прогресса
     */
    override fun setProgressVisibility(isVisible: Boolean) {
        isProgressVisible = isVisible
        forEachView { it.setProgressVisibility(isVisible) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisability(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisability(isVisible) }
    }
    /**
     * Прокрутка списка
     */
    override fun scrollRecycler(position: Int) {
        this.position = position
        forEachView { it.scrollRecycler(position) }
    }
}