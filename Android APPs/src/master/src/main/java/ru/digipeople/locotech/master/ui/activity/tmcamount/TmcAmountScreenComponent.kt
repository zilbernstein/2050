package ru.digipeople.locotech.master.ui.activity.tmcamount

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент ввода/изменения количества ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface TmcAmountScreenComponent {
    fun component(): TmcAmountComponent.Builder
}