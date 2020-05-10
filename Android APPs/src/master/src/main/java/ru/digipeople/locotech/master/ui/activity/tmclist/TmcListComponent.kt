package ru.digipeople.locotech.master.ui.activity.tmclist

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent( modules = [ActivityModule::class])
interface TmcListComponent {
    fun inject(activity: TmcListActivity)
    fun presenter(): TmcListPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun workId(worId: String): Builder

        fun build(): TmcListComponent
    }
}