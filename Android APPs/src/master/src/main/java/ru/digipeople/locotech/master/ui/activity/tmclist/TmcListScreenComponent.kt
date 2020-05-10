package ru.digipeople.locotech.master.ui.activity.tmclist

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface TmcListScreenComponent {
    fun component(): TmcListComponent.Builder
}