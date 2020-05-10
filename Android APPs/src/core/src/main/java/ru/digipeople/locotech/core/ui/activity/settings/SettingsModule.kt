package ru.digipeople.locotech.core.ui.activity.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль для экрана настроек
 */
@Module
class SettingsModule {

    @Provides
    fun viewModelFactory(provider: Provider<SettingsViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}