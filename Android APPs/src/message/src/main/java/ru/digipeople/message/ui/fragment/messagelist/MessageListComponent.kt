package ru.digipeople.message.ui.fragment.messagelist

import dagger.Subcomponent
import ru.digipeople.ui.dagger.FragmentScope

/**
 * Компонент для фрагмента списка сообщений
 *
 * @author Kashonkov Nikita
 */
@FragmentScope
@Subcomponent()
interface MessageListComponent{
    fun inject(fragment: MessageListFragment)
    fun presenter(): MessageListPresenter
}