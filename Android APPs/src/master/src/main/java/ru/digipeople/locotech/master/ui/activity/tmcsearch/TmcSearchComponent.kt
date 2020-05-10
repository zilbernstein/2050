package ru.digipeople.locotech.master.ui.activity.tmcsearch

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TmcSearchComponent {
    fun inject(activity: TmcSearchActivity)
    fun presenter(): TmcSearchPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun workId(worId: String): Builder

        @BindsInstance
        fun tmcIdList(tmcIdList: ArrayList<String>): Builder

        fun build(): TmcSearchComponent
    }

}