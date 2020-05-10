package ru.digipeople.locotech.master.ui.activity.searchperformer

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface SearchPerformerScreenComponent {
    fun  componentBuilder(): SearchPerformerComponent.Builder
}