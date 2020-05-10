package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * компонент замечаний
 *
 * @author Michael Solenov
 */
@ActivityScope
@Component(
        modules = [
            MeasurementConfirmationModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ])

interface MeasurementConfirmationComponent {
    fun inject(activity: MeasurementConfirmationActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}