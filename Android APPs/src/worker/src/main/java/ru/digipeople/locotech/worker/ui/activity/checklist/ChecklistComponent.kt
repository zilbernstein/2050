package ru.digipeople.locotech.worker.ui.activity.checklist

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент чеклиста
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Component(
        modules = [
            ChecklistModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            UiComponent::class
        ])

interface ChecklistComponent {
    fun inject(checklistActivity: ChecklistActivity)
    fun viewModelFactory(): ViewModelProvider.Factory
}