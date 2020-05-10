package ru.digipeople.locotech.technologist.ui.activity.remarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль замечаний
 */
@Module
class RemarksModule {
    @Provides
    fun viewModelFactory(provider: Provider<RemarksViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}