package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль выбора причины обточки
 *
 * @author Michael Solenov
 */
@Module
class TuningReasonsModule {
    @Provides
    fun viewModelFactory(provider: Provider<TuningReasonsViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}