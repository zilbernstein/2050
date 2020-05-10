package ru.digipeople.locotech.master.ui.activity.checkwork

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface CheckWorkScreenComponent {
    fun componentBuilder(): CheckWorkComponent.Builder
}