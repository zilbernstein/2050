package ru.digipeople.locotech.worker.ui.activity.shift

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры смены
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ShiftComponent {

    fun inject(shiftActivity: ShiftActivity)

    fun presenter(): ShiftPresenter
}