package ru.digipeople.message.ui.activity.messagelist

import dagger.Subcomponent
import ru.digipeople.message.ui.fragment.messagelist.MessageListComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент списка сообщений
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface MessageListComponent {
    fun inject(activity: MessageListActivity)
    fun presenter(): MessageListPresenter
    fun messageListComponent(): MessageListComponent
}