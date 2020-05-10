package ru.digipeople.locotech.metrologist.ui.activity.measurementdetail

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент детальной информации замера
 */
@ActivityScope
@Component(
        modules = [
            MeasurementDetailModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ])

interface MeasurementDetailComponent {
    fun inject(measurementDetailActivity: MeasurementDetailActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}