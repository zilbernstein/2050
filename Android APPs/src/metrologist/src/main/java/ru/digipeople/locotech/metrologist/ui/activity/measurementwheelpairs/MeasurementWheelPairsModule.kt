package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль ввода данных замера
 */
@Module
class MeasurementWheelPairsModule {
    @Provides
    fun viewModelFactory(provider: Provider<MeasurementWheelPairsViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}