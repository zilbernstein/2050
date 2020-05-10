package ru.digipeople.message.ui.activity.message

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViesState сообщения
 *
 * @author Kashonkov Nikita
 */
class MessageViewState @Inject constructor() : BaseMvpViewState<MessageView>(), MessageView {
    override fun onViewAttached(view: MessageView?) {}

    override fun onViewDetached(view: MessageView?) {}
    /**
     * Переход к новому сообщению
     */
    override fun navigateToNewMessage() {
        forEachView { it.navigateToNewMessage() }
    }
}