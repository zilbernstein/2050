package ru.digipeople.locotech.metrologist.ui.activity.measurementdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль детальной информации замера
 */
@Module
class MeasurementDetailModule {
    @Provides
    fun viewModelFactory(provider: Provider<MeasurementDetailViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}