package ru.digipeople.locotech.master.ui.activity.measurementedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль редактирования замера
 */
@Module
class MeasurementEditModule {
    @Provides
    fun viewModelFactory(provider: Provider<MeasurementEditViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}