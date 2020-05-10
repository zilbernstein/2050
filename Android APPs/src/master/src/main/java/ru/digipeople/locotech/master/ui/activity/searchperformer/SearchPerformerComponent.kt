package ru.digipeople.locotech.master.ui.activity.searchperformer

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface SearchPerformerComponent {
    fun inject(searchPerformerActivity: SearchPerformerActivity)

    fun presenter(): SearchPerformerPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun params(params: SearchPerformerParams): Builder

        fun build(): SearchPerformerComponent
    }
}