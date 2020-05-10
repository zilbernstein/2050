package ru.digipeople.locotech.worker.ui.activity.choosereason

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент экрана структуры выбора причины
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface ChooseReasonScreenComponent {
    fun componentBuilder(): ChooseReasonComponent.Builder
}