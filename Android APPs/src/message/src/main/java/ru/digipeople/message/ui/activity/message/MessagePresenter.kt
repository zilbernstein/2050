package ru.digipeople.message.ui.activity.message

import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер сообщения
 *
 * @author Kashonkov Nikita
 */
class MessagePresenter @Inject constructor(state: MessageViewState) : BaseMvpViewStatePresenter<MessageView, MessageViewState>(state) {

    override fun onInitialize() {

    }
    /**
     * Обработка перехода к новому сообщению
     */
    fun onCreateMessageClicked() {
        view.navigateToNewMessage()
    }
}