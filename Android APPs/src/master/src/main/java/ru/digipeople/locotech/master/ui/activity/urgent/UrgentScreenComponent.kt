package ru.digipeople.locotech.master.ui.activity.urgent

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент срочно
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface UrgentScreenComponent {

    fun urgentComponent(activityModule: ActivityModule): UrgentComponent
}