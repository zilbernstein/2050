package ru.digipeople.locotech.inspector.ui.activity.print

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент печати
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface PrintScreenComponent {
    fun component(module: ActivityModule): PrintComponent
}