package ru.digipeople.locotech.master.ui.activity.performance

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 *Экранный компонент исполнения
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface PerformanceScreenComponent {

    fun perfomanceComponent(activityModule: ActivityModule): PerformanceComponent
}