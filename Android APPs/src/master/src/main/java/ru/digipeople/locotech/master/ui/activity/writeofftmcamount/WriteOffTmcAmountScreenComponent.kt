package ru.digipeople.locotech.master.ui.activity.writeofftmcamount

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface WriteOffTmcAmountScreenComponent {
    fun component(): WriteOffTmcAmountComponent.Builder
}