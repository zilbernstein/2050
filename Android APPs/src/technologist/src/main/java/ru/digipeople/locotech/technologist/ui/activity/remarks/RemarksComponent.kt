package ru.digipeople.locotech.technologist.ui.activity.remarks

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры замечаний
 */
@ActivityScope
@Component(
        modules = [
            RemarksModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ])
interface RemarksComponent {
    fun inject(activity: RemarksActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}