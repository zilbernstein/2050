package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface TmcBeforeAcceptScreenComponent {
    fun component(): TmcBeforeAcceptComponent.Builder
}