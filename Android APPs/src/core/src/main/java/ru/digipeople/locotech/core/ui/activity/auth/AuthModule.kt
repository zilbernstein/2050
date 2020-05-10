package ru.digipeople.locotech.core.ui.activity.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Модуль структуры авторизации
 *
 * @author Nikita Sychev
 **/
@Module
class AuthModule {

    @Provides
    fun viewModelFactory(provider: Provider<AuthViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}