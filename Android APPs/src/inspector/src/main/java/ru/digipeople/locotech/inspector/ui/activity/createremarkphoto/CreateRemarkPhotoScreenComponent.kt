package ru.digipeople.locotech.inspector.ui.activity.createremarkphoto

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент создания нового замечания
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface CreateRemarkPhotoScreenComponent {
    fun component(activityModule: ActivityModule): CreateRemarkPhotoComponent
}