package ru.digipeople.message.ui.activity.messagelist

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState списка сообщений
 *
 * @author Kashonkov Nikita
 */
class MessageListViewState @Inject constructor() : BaseMvpViewState<MessageListView>(), MessageListView {
    override fun onViewAttached(view: MessageListView?) {}
    override fun onViewDetached(view: MessageListView?) {}
    /**
     * Переход к деталке сообщения
     */
    override fun navigateToMessageDetail() {
        forEachView { it.navigateToMessageDetail() }
    }
    /**
     * Переход к написанию нового сообщения
     */
    override fun navigateToWriteMessage() {
        forEachView { it.navigateToWriteMessage() }
    }
}