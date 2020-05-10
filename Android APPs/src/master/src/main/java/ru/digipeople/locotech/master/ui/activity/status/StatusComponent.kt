package ru.digipeople.locotech.master.ui.activity.status

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент статуса работ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface StatusComponent {

    fun inject(statusActivity: StatusActivity)

    fun statusPresenter(): StatusPresenter
}