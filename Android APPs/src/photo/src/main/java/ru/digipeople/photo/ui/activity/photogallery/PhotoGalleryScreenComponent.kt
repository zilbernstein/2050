package ru.digipeople.photo.ui.activity.photogallery

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры фотогаллерея
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface PhotoGalleryScreenComponent {
    fun componentBuilder(): PhotoGalleryComponent.Builder
}