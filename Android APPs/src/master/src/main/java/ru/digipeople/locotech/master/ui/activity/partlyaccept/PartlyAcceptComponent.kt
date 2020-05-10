package ru.digipeople.locotech.master.ui.activity.partlyaccept

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент частичной приемки
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface PartlyAcceptComponent {
    fun inject(activity: PartlyAcceptActivity)

    fun presenter(): PartlyAcceptPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun params(commentParams: PartlyAcceptParams): Builder

        fun build(): PartlyAcceptComponent
    }
}