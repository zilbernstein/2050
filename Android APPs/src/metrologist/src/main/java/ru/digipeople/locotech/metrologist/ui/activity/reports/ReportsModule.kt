package ru.digipeople.locotech.metrologist.ui.activity.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль отчетов
 *
 * @author Michael Solenov
 */
@Module
class ReportsModule {

    @Provides
    fun viewModelFactory(provider: Provider<ReportsViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}