package ru.digipeople.locotech.inspector.ui.activity.measurement

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент замеров
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface MeasurementComponent {
    fun inject(measurementActivity: MeasurementActivity)

    fun presenter(): MeasurementPresenter
}