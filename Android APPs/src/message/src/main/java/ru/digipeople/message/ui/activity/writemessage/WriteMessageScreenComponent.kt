package ru.digipeople.message.ui.activity.writemessage

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент экрана отправки сообщения
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface WriteMessageScreenComponent {
    fun component(activityModule: ActivityModule): WriteMessageComponent
}