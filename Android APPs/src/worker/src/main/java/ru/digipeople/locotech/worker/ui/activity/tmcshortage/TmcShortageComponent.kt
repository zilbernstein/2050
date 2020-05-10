package ru.digipeople.locotech.worker.ui.activity.tmcshortage

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TmcShortageComponent {
    fun inject(activity: TmcShortageActivity)

    fun presenter(): TmcShortagePresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): TmcShortageComponent.Builder

        @BindsInstance
        fun workId(workId: String): TmcShortageComponent.Builder

        fun build(): TmcShortageComponent
    }

}