package ru.digipeople.locotech.master.ui.activity.divide

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент разделения работы
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface DivideScreenComponent {
    fun componentBuilder(): DivideComponent.Builder
}