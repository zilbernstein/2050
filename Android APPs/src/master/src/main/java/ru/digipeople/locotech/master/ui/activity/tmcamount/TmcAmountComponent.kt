package ru.digipeople.locotech.master.ui.activity.tmcamount

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент ввода/изменения количества ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TmcAmountComponent {
    fun inject(activity: TmcAmountActivity)
    fun presenter(): TmcAmountPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun params(params: TmcAmountParams): Builder

        fun build(): TmcAmountComponent
    }

}