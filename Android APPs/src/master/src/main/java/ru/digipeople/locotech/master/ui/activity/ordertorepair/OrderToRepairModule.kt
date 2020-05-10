package ru.digipeople.locotech.master.ui.activity.ordertorepair

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль запрос на получение ТМЦ для ремонта
 */
@Module
class OrderToRepairModule {
    @Provides
    fun viewModelFactory(viewModelProvider: Provider<OrderToRepairViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}