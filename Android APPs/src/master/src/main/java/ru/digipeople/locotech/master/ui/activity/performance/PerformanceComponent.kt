package ru.digipeople.locotech.master.ui.activity.performance

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент исполнения
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface PerformanceComponent {
    fun inject(performanceActivity: PerformanceActivity)

    fun performancePresenter(): PerformancePresenter
}