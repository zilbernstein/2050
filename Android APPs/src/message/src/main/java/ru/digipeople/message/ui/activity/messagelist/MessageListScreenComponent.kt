package ru.digipeople.message.ui.activity.messagelist

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент экрана списка сообщений
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface MessageListScreenComponent {
    fun component(activityModule: ActivityModule): MessageListComponent
}