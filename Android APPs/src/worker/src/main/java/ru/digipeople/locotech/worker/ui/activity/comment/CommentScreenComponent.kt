package ru.digipeople.locotech.worker.ui.activity.comment

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры комментария
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface CommentScreenComponent {

    fun componentBuilder(): CommentComponent.Builder

}