package ru.digipeople.locotech.master.ui.activity.groupassignment

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**Экранный компонент группового назначения исполнителей
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
@Subcomponent
interface GroupAssignmentScreenComponent {
    fun groupAssignmentComponent(activityModule: ActivityModule): GroupAssignmentComponent
}