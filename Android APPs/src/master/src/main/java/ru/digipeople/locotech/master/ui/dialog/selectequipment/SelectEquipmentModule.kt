package ru.digipeople.locotech.master.ui.dialog.selectequipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль диалога выбора оборудования из бокового меню.
 */
@Module
class SelectEquipmentModule {
    @Provides
    fun viewModelFactory(provider: Provider<SelectEquipmentViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}