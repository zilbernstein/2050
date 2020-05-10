package ru.digipeople.locotech.technologist.ui.activity.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль работ 
 */
@Module
class WorkModule {
    @Provides
    fun viewModelFactory(provider: Provider<WorkViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}