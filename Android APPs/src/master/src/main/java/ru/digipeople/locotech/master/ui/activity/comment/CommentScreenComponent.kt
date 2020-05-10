package ru.digipeople.locotech.master.ui.activity.comment

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент комментариев
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface CommentScreenComponent {
    fun componentBuilder(): CommentComponent.Builder
}