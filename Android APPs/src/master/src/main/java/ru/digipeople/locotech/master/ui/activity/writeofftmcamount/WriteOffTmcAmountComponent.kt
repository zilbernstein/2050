package ru.digipeople.locotech.master.ui.activity.writeofftmcamount

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface WriteOffTmcAmountComponent {
    fun inject(activity: WriteOffTmcAmountActivity)
    fun presenter(): WriteOffTmcAmountPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun params(params: WriteOffTmcAmountParams): Builder

        fun build(): WriteOffTmcAmountComponent
    }

}