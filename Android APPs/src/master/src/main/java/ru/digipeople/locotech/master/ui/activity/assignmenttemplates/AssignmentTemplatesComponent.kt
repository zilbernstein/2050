package ru.digipeople.locotech.master.ui.activity.assignmenttemplates

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент выбора шаблона исполнителей
 */
@ActivityScope
@Subcomponent(modules = [
    ActivityModule::class,
    AssignmentTemplatesModule::class
])
interface AssignmentTemplatesComponent {
    fun inject(assignmentTemplatesActivity: AssignmentTemplatesActivity)

    fun viewModelFactory(): ViewModelProvider.Factory
}