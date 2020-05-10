package ru.digipeople.locotech.master.ui.activity.workerspresence

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент явки сотрудников
 */
@ActivityScope
@Subcomponent(modules = [
    ActivityModule::class,
    WorkersPresenceModule::class
])
interface WorkersPresenceComponent {
    fun inject(workersPresenceActivity: WorkersPresenceActivity)

    fun viewModelFactory(): ViewModelProvider.Factory
}