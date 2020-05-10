package ru.digipeople.locotech.metrologist.ui.activity.reports

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент отчетов
 *
 * @author Michael Solenov
 */
@ActivityScope
@Component(
        modules = [
            ReportsModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ]
)
interface ReportsComponent {
    fun inject(reportsActivity: ReportsActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}