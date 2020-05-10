package ru.digipeople.photo.ui.activity.photogallery

/**
 * Enum для выбора параметра запуска фотогалерии
 *
 * @author Kashonkov Nikita
 */
enum class PhotoGalleryStartMode {
    /**
     * Только просмотр фотографий для замечаний
     */
    VIEW_MODE_REMARK,
    /**
     * С возможностью съемки
     */
    PHOTO_MODE_REMARK,
    /**
     * Только просмотр фотографий
     */
    VIEW_MODE_WORK,
    /**
     * С возможностью съемки
     */
    PHOTO_MODE_WORK,
    /**
    * Только просмотр фотографий
    */
    VIEW_MODE_CSO,
    /**
     * С возможностью съемки
     */
    PHOTO_MODE_CSO
}