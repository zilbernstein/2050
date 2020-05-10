package ru.digipeople.locotech.master.ui.dialog.selectequipment

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.dagger.FragmentScope
/**
 * Компонент диалога выбора оборудования из бокового меню.
 */
@FragmentScope
@Component(
        modules = [
            SelectEquipmentModule::class
        ],
        dependencies = [
            AppComponent::class,
            CoreAppComponent::class,
            UiComponent::class
        ])

interface SelectEquipmentComponent {
    fun inject(fragment: SelectEquipmentDialog)
    fun viewModelFactory(): ViewModelProvider.Factory
}