package ru.digipeople.locotech.worker.ui.activity.measurements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль замеры
 */
@Module
class MeasurementsModule {
    @Provides
    fun provideViewModelFactory(viewModelProvider: Provider<MeasurementsViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}