package ru.digipeople.message.ui.fragment.messagedetail

import dagger.Subcomponent
import ru.digipeople.ui.dagger.FragmentScope

/**
 * Компонет для фрагмента деталки сообщения
 */
@FragmentScope
@Subcomponent
interface MessageDetailComponent {
    fun inject(fragment: MessageDetailFragment)
    fun presenter(): MessageDetailPresenter
}