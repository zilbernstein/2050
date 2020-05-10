package ru.digipeople.locotech.core.ui.activity.auth

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent

/**
 * Компонент структуры авторизации
 *
 * @author Nikita Sychev
 **/
@Component(
        modules = [
            AuthModule::class
        ],
        dependencies = [
            CoreAppComponent::class
        ]
)
interface AuthComponent {
    fun inject(authActivity: AuthActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}