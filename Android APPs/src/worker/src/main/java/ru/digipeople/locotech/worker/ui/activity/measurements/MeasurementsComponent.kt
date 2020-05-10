package ru.digipeople.locotech.worker.ui.activity.measurements

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент для замеров
 */
@ActivityScope
@Subcomponent(
        modules = [
            ActivityModule::class,
            MeasurementsModule::class
        ]
)
interface MeasurementsComponent {
    fun inject(measureActivity: MeasurementsActivity)

    fun viewModelFactory(): ViewModelProvider.Factory
}