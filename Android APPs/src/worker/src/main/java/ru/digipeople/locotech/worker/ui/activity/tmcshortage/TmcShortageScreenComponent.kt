package ru.digipeople.locotech.worker.ui.activity.tmcshortage

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface TmcShortageScreenComponent {
    fun componentBuilder(): TmcShortageComponent.Builder
}