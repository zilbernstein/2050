package ru.digipeople.photo.ui.activity.photogallery

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры фотогаллерея
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface PhotoGalleryComponent {
    fun inject(photoGalleryActivity: PhotoGalleryActivity)

    fun presenter(): PhotoGalleryPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun id(id: String): Builder

        @BindsInstance
        fun startMode(startType: PhotoGalleryStartMode): Builder

        fun build(): PhotoGalleryComponent
    }
}