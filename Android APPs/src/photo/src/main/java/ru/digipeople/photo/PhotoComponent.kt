package ru.digipeople.photo

import ru.digipeople.photo.api.ThingWorxWorker
import ru.digipeople.photo.helper.PhotoFileStorage
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryScreenComponent

/**
 * Интерфейс модуля photo
 *
 * @author Kashonkov Nikita
 */
interface PhotoComponent {
    /**
     * Экранный компонент структуры фотогаллерея
     */
    fun photoGalleryScreenComponent(): PhotoGalleryScreenComponent
    /**
     * Интерфейс для работы с Апи
     */
    fun thingWorxWorker(): ThingWorxWorker

    companion object {
        private lateinit var instance: PhotoComponent

        fun get(): PhotoComponent = instance

        fun set(photoComponent: PhotoComponent) {
            instance = photoComponent
        }
    }
}