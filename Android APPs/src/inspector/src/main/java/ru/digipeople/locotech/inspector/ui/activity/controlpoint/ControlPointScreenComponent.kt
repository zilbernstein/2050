package ru.digipeople.locotech.inspector.ui.activity.controlpoint

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент контрольных точек
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface ControlPointScreenComponent {
    fun component(): ControlPointComponent.Builder
}