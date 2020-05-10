package ru.digipeople.message.ui.activity.messagedetail

import dagger.Subcomponent
import ru.digipeople.message.ui.fragment.messagedetail.MessageDetailComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент деталки сообщений
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface MessageDetailComponent {
    fun inject(messageDetailActivity: MessageDetailActivity)
    fun presenter(): MessageDetailPresenter
    fun messageDetailComponent(): MessageDetailComponent
}