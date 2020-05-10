package ru.digipeople.locotech.master.ui.activity.worklist

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент добавления замечания / работ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface WorkListComponent {

    fun inject(workListActivity: WorkListActivity)

    fun presenter(): WorkListPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun remarkId(remarkId: String): Builder

        fun build(): WorkListComponent
    }

}