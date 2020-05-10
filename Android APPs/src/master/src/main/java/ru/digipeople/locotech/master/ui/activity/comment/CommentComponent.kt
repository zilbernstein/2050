package ru.digipeople.locotech.master.ui.activity.comment

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент комментариев
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface CommentComponent {

    fun inject(activity: CommentActivity)

    fun presenter(): CommentPresenter

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun commentParams(commentParams: CommentParams): Builder

        @BindsInstance
        fun callingId(callingId: Int): Builder

        fun build(): CommentComponent
    }
}