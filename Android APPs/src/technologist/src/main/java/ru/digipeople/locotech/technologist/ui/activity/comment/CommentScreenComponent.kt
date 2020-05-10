package ru.digipeople.locotech.technologist.ui.activity.comment

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент экрана структуры замечаний
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
@Subcomponent
interface CommentScreenComponent {

    fun componentBuilder(): CommentComponent.Builder

}