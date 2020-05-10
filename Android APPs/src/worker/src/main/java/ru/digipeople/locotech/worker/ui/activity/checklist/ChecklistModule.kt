package ru.digipeople.locotech.worker.ui.activity.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль создающий viewModel для структуры чеклист
 *
 * @author Sostavkin Grisha
 */
@Module
class ChecklistModule {
    @Provides
    fun viewModelFactory(provider: Provider<ChecklistViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}