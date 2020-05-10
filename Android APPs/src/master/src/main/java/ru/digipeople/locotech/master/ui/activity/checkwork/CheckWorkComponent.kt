package ru.digipeople.locotech.master.ui.activity.checkwork

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface CheckWorkComponent {
    fun inject(checkWorkActivity: CheckWorkActivity)

    fun presenter(): CheckWorkPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun callingType(callingType: CheckWorkCallingType): Builder

        fun build(): CheckWorkComponent
    }
}