package ru.digipeople.locotech.inspector.ui.activity.createremarkphoto

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент создания нового замечания
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface CreateRemarkPhotoComponent {
    fun inject(activity: CreateRemarkPhotoActivity)
}