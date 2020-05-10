package ru.digipeople.locotech.inspector.ui.activity.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль локомотивов
 */
@Module
class EquipmentModule {
    @Provides
    fun viewModelFactory(provider: Provider<EquipmentViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}