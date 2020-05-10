package ru.digipeople.locotech.master.ui.activity.groupassignment

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент группового назначения исполнителей
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface GroupAssignmentComponent {
    fun inject(groupAssignmentActivity: GroupAssignmentActivity)

    fun presenter(): GroupAssignmentPresenter
}