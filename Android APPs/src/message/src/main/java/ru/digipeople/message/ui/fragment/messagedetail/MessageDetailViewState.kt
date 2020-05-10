package ru.digipeople.message.ui.fragment.messagedetail

import ru.digipeople.message.model.Message
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState для фрагмента деталки сообщения 
 */
class MessageDetailViewState @Inject constructor() : BaseMvpViewState<MessageDetailView>(), MessageDetailView {

    var message: Message? = null

    override fun onViewAttached(view: MessageDetailView) {
        view.showMessage(message)
    }

    override fun onViewDetached(view: MessageDetailView) {}
    /**
     * показать сообщение
     */
    override fun showMessage(message: Message?) {
        this.message = message
        forEachView { it.showMessage(message) }
    }
    /**
     * переход к пересылке сообщения
     */
    override fun navigateToResendMessage() {
        forEachView { it.navigateToResendMessage() }
    }
}