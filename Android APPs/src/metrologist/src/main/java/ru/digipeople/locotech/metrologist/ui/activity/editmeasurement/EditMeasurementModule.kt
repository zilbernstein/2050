package ru.digipeople.locotech.metrologist.ui.activity.editmeasurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль редактирования данных по кп
 *
 * @author Michael Solenov
 */
@Module
class EditMeasurementModule {
    @Provides
    fun viewModelFactory(provider: Provider<EditMeasurementViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}