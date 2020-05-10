package ru.digipeople.locotech.inspector.ui.activity.controlpoint

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент контрольных точек
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ControlPointComponent {
    fun inject(activity: ControlPointActivity)
    fun presenter(): ControlPointPresenter

    @Subcomponent.Builder
     interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun workId(workId: String): Builder

        fun build(): ControlPointComponent
    }
}