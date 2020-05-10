package ru.digipeople.message.ui.activity.messagedetail

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент экрана деталки сообщения
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface MessageDetailScreenComponent {
    fun component(activityModule: ActivityModule): MessageDetailComponent
}