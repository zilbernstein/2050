package ru.digipeople.message.ui.activity.writemessage

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент отправки сообщения
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface WriteMessageComponent {
    fun inject(activity: WriteMessageActivity)
    fun presenter(): WriteMessagePresenter
}