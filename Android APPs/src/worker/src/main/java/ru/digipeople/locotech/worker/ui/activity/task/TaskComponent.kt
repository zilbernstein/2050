package ru.digipeople.locotech.worker.ui.activity.task

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры задания
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TaskComponent {

    fun inject(taskActivity: TaskActivity)

    fun presenter(): TaskPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): TaskComponent.Builder

        @BindsInstance
        fun workId(workId: String): TaskComponent.Builder

        fun build(): TaskComponent
    }
}