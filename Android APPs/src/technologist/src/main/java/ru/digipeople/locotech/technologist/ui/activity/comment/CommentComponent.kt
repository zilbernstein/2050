package ru.digipeople.locotech.technologist.ui.activity.comment

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры замечаний
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface CommentComponent {

    fun inject(activity: CommentActivity)

    fun presenter(): CommentPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): CommentComponent.Builder

        @BindsInstance
        fun commentParams(commentParams: CommentParams): CommentComponent.Builder

        fun build(): CommentComponent
    }
}