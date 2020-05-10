package ru.digipeople.locotech.core.ui.activity.settings

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент экрана настроек приложения
 *
 * @author Aleksandr Brazhkin
 */
@ActivityScope
@Component(
        modules = [
            ActivityModule::class,
            SettingsModule::class
        ],
        dependencies = [
            CoreAppComponent::class
        ]
)
interface SettingsComponent {
    fun inject(settingsActivity: SettingsActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}