package ru.digipeople.locotech.master.ui.activity.approved

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент согласование
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ApprovedComponent {
    fun inject(appovedActivity: ApprovedActivity)

    fun approvedPresenter(): ApprovedPresenter
}