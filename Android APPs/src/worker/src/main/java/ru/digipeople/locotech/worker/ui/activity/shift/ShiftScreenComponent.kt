package ru.digipeople.locotech.worker.ui.activity.shift

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры смены
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface ShiftScreenComponent {

    fun component(activityModule: ActivityModule): ShiftComponent
}