package ru.digipeople.locotech.metrologist.ui.activity.editmeasurement

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * компонент редактирования данных по кп
 *
 * @author Michael Solenov
 */
@ActivityScope
@Component(
        modules = [
            EditMeasurementModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ])
interface EditMeasurementComponent {
    fun inject(activity: EditMeasurementActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}