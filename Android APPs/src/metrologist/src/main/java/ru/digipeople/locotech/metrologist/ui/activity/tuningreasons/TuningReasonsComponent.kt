package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент выбора причины обточки
 *
 * @author Michael Solenov
 */
@ActivityScope
@Component(
        modules = [
            TuningReasonsModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ]
)
interface TuningReasonsComponent {
    fun inject(tuningReasonsActivity: TuningReasonsActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}
