package ru.digipeople.locotech.worker.ui.activity.mytask

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры работы
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface MyTaskScreenComponent {

    fun component(activityModule: ActivityModule): MyTaskComponent
}