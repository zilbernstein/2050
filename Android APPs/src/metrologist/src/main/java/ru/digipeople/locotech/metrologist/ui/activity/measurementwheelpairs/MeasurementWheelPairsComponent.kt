package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент ввода данных замера
 */
@ActivityScope
@Component(
        modules = [
            MeasurementWheelPairsModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ])

interface MeasurementWheelPairsComponent {
    fun inject(activity: MeasurementWheelPairsActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}