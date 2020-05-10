package ru.digipeople.locotech.inspector.ui.activity.declinereason

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент причин удаления замечания
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface DeclineReasonScreenComponent {
    fun componentBuilder(): DeclineReasonComponent.Builder
}