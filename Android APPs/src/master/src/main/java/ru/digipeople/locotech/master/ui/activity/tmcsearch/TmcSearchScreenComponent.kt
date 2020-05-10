package ru.digipeople.locotech.master.ui.activity.tmcsearch

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface TmcSearchScreenComponent {
    fun component(): TmcSearchComponent.Builder
}