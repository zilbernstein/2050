package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент списание ТМЦ
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TmcBeforeAcceptComponent {
    fun inject(activity: TMCBeforeAcceptActivity)
    fun presenter(): TMCBeforeAcceptPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun workIds(workIds: ArrayList<String>): Builder

        fun build(): TmcBeforeAcceptComponent
    }
}