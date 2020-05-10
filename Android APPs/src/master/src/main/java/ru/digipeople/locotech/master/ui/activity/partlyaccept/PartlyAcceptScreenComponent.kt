package ru.digipeople.locotech.master.ui.activity.partlyaccept

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонентчастичной приемки
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface PartlyAcceptScreenComponent {
    fun componentBuilder(): PartlyAcceptComponent.Builder
}