package ru.digipeople.locotech.master.ui.activity.allworklist

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент списка работ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface AllWorkScreenComponent {
    fun componentBuilder(): AllWorkComponent.Builder
}