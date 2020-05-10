package ru.digipeople.locotech.inspector.ui.activity.declinereason

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент причин удаления замечания
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface DeclineReasonComponent {
    fun inject(acivity: DeclineReasonActivity)
    fun presenter(): DeclineReasonPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun remarkId(remarkId: String): Builder

        fun build(): DeclineReasonComponent
    }
}