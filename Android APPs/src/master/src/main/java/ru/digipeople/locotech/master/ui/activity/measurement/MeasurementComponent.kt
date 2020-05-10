package ru.digipeople.locotech.master.ui.activity.measurement

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент замеров
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [
    ActivityModule::class,
    MeasurementModule::class
])
interface MeasurementComponent {
    fun inject(measurementActivity: MeasurementActivity)

    fun viewModelFactory(): ViewModelProvider.Factory
}