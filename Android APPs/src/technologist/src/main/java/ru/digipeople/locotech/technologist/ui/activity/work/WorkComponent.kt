package ru.digipeople.locotech.technologist.ui.activity.work

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент работы
 */
@ActivityScope
@Component(
        modules = [
            WorkModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ])
interface WorkComponent {
    fun inject(activity: WorkActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}