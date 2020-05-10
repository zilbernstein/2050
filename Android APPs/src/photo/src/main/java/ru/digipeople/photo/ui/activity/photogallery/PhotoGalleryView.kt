package ru.digipeople.photo.ui.activity.photogallery

import android.net.Uri
import ru.digipeople.photo.model.Photo
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс структуры фотогаллерея
 *
 * @author Kashonkov Nikita
 */
interface PhotoGalleryView : MvpView {
    /**
     * Установить фото
     */
    fun setPhoto(photo: Photo?)
    /**
     * переход к созданию фото
     */
    fun navigateToCreatePhoto(uri: Uri)
    /**
     * Установка данных в адаптер
     */
    fun setDataToAdapter(list: List<Photo>)
    /**
     * Управление видимостью кнопки
     */
    fun changeCreatePhotoButtonVisability(isVisible: Boolean)
    /**
     * Переход назад
     */
    fun navigateBack()
    /**
     * Переход к подтверждению
     */
    fun navigateOnCheckButton()
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Проверка разрешений
     */
    fun checkPermissions()
    /**
     * Прокрутка списка
     */
    fun scrollRecycler(position: Int)
    /**
     * Управдление видимостью лоадера
     */
    fun setLoadingVisability(isVisible: Boolean)
    /**
     * Управление видимостью прогресса
     */
    fun setProgressVisibility(isVisible: Boolean)
    /**
     * Установка прогресса
     */
    fun setSendingProgress(currentProgress: Int, maxProgress: Int)

    fun setUpMode(startType: PhotoGalleryStartMode)
}