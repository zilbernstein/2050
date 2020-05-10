package ru.digipeople.message.ui.activity.message

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент экрана сообщения
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface MessageScreenComponent {

    fun messageComponent(activityModule: ActivityModule): MessageComponent
}