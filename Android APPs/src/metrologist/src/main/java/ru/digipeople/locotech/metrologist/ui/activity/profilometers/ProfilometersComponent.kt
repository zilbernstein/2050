package ru.digipeople.locotech.metrologist.ui.activity.profilometers

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент профилометров
 */
@ActivityScope
@Component(
        modules = [
            ActivityModule::class,
            ProfilometersModule::class
        ],
        dependencies = [
            UiComponent::class,
            AppComponent::class
        ]
)

interface ProfilometersComponent {
    fun inject(profilometresActivity: ProfilometersActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}