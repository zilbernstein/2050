package ru.digipeople.locotech.inspector.ui.activity.measurement

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент замеров
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
@Subcomponent
interface MeasurementScreenComponent {
    fun measurementsComponent(activityModule: ActivityModule): MeasurementComponent
}