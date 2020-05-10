package ru.digipeople.locotech.metrologist.ui.activity.measurementtypes

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент типов замеров
 */
@ActivityScope
@Component(
        modules = [
            MeasurementTypesModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ]
)
interface MeasurementTypesComponent {
    fun inject(measurementTypesActivity: MeasurementTypesActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}