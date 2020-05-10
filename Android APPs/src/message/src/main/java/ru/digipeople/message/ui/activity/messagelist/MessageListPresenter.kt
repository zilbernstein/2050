package ru.digipeople.message.ui.activity.messagelist

import ru.digipeople.message.helper.CurrentMessageProvider
import ru.digipeople.message.model.Message
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер списка сообщений
 *
 * @author Kashonkov Nikita
 */
class MessageListPresenter @Inject constructor(viewState: MessageListViewState,
                                               private val currentMessageProvider: CurrentMessageProvider) : BaseMvpViewStatePresenter<MessageListView, MessageListViewState>(viewState) {
    override fun onInitialize() {}

    override fun destroy() {
        super.destroy()
        currentMessageProvider.message = null
    }
    /**
     * Обработка нажатия на сообщение
     */
    fun onMessageClicked(message: Message) {
        currentMessageProvider.message = message
        view.navigateToMessageDetail()
    }
    /**
     * Обработка создания нового сообщения
     */
    fun onCreateNewMessageClicked() {
        view.navigateToWriteMessage()
    }
}