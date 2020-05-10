package ru.digipeople.locotech.master.ui.activity.urgent

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент срочно
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface UrgentComponent {
    fun inject(urgentActivity: UrgentActivity)

    fun urgentPresenter(): UrgentPresenter
}