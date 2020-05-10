package ru.digipeople.message.ui.activity.message

import dagger.Subcomponent
import ru.digipeople.message.ui.fragment.messagedetail.MessageDetailComponent
import ru.digipeople.message.ui.fragment.messagelist.MessageListComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент сообщения
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface MessageComponent {
    fun inject(messageActivity: MessageActivity)
    fun messagePresenter(): MessagePresenter
    fun messageListComponent(): MessageListComponent
    fun messageDetailComponent(): MessageDetailComponent
}