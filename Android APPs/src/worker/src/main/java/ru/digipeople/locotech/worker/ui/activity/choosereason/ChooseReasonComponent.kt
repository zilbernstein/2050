package ru.digipeople.locotech.worker.ui.activity.choosereason

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры выбора причины
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ChooseReasonComponent {

    fun inject(activity: ChooseReasonActivity)

    fun presenter(): ChooseReasonPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): ChooseReasonComponent.Builder

        @BindsInstance
        fun workId(workId: String): ChooseReasonComponent.Builder

        fun build(): ChooseReasonComponent
    }

}