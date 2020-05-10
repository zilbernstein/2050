package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль замечаний
 *
 * @author Michael Solenov
 */
@Module
class MeasurementConfirmationModule {
    @Provides
    fun viewModelFactory(provider: Provider<MeasurementConfirmationViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}