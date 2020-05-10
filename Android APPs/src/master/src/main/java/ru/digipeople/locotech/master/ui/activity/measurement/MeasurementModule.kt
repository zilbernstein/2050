package ru.digipeople.locotech.master.ui.activity.measurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль замеров
 *
 * @author Sostavkin Grisha
 */
@Module
class MeasurementModule {
    @Provides
    fun viewModelFactory(viewModelProvider: Provider<MeasurementViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}