package ru.digipeople.locotech.inspector.ui.activity.equipment

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент локомотивов
 */
@ActivityScope
@Component(
        modules = [
            EquipmentModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            CoreAppComponent::class,
            UiComponent::class
        ])
interface EquipmentComponent {
    fun inject(activity: EquipmentActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}