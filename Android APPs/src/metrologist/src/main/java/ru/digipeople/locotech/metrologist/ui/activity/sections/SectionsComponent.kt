package ru.digipeople.locotech.metrologist.ui.activity.sections

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент секций
 *
 * @author Nikita Sychev
 **/
@ActivityScope
@Component(
        modules = [
            ActivityModule::class,
            SectionsModule::class
        ],
        dependencies = [
            UiComponent::class,
            AppComponent::class
        ]
)
interface SectionsComponent {
    fun inject(sectionsActivity: SectionsActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}