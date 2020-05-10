package ru.digipeople.locotech.inspector.ui.activity.repairbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль книги ремонтов
 */
@Module
class RepairBookModule {
    @Provides
    fun viewModelFactory(provider: Provider<RepairBookViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}