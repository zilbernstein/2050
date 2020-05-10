package ru.digipeople.locotech.master.ui.activity.divide

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент разделения работы
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface DivideComponent {
    fun inject(activity: DivideActivity)

    fun presenter(): DividePresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun params(commentParams: DivideParams): Builder

        fun build(): DivideComponent
    }
}