package ru.digipeople.locotech.inspector.ui.activity.inspection

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент инспекционного контроля
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface InspectionScreenComponent {
    fun component(activityModule: ActivityModule): InspectionComponent
}