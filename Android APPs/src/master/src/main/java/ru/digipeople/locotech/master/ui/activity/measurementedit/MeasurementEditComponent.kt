package ru.digipeople.locotech.master.ui.activity.measurementedit

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент редактирования замера
 */
@ActivityScope
@Component(
        modules = [
            MeasurementEditModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            CoreAppComponent::class,
            UiComponent::class
        ])

interface MeasurementEditComponent {
    fun inject(measurementEditActivity: MeasurementEditActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}