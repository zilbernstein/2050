package ru.digipeople.locotech.master.ui.activity.approved

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Экранный компонент согласование
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface ApprovedScreenComponent {
    fun approvedComponent(activityModule: ActivityModule): ApprovedComponent
}