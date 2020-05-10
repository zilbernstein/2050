package ru.digipeople.locotech.inspector.ui.activity.repairbook

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент книги ремонтов
 */
@ActivityScope
@Component(
        modules = [
            RepairBookModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            CoreAppComponent::class,
            UiComponent::class
        ])

interface RepairBookComponent {
    fun inject(activity: RepairBookActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}