package ru.digipeople.message.ui.fragment.messagedetail

import io.reactivex.disposables.Disposables
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.message.helper.CreatingMessageProvider
import ru.digipeople.message.helper.CurrentMessageProvider
import ru.digipeople.message.model.Contact
import ru.digipeople.message.model.CreatingMessage
import ru.digipeople.message.ui.fragment.messagedetail.interactor.MessageReader
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер для фрагмента деталки сообщения
 */
class MessageDetailPresenter @Inject constructor(
        viewState: MessageDetailViewState,
        private val messageReader: MessageReader,
        private val currentMessageProvider: CurrentMessageProvider,
        private val creatingMessageProvider: CreatingMessageProvider
) : BaseMvpViewStatePresenter<MessageDetailView, MessageDetailViewState>(viewState) {
    /**
     * подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(MessageDetailPresenter::class)

    private var messageChangesDisposable = Disposables.disposed()
    private var sendMessageDisposable = Disposables.disposed()
    private var markAsReadDisposable = Disposables.disposed()
    /**
     * действия при инициализации
     */
    override fun onInitialize() {
        messageChangesDisposable = currentMessageProvider.messageChanges
                .subscribe { ref ->
                    val message = ref.get()
                    message?.let {
                        markAsRead(it.id)
                    }
                    view.showMessage(message)
                }
    }
    /**
     * действия при уничтожении
     */
    override fun destroy() {
        messageChangesDisposable.dispose()
        markAsReadDisposable.dispose()
        sendMessageDisposable.dispose()
    }
    /**
     * отметка как прочитанное
     */
    private fun markAsRead(id: String) {
        markAsReadDisposable.dispose()
        markAsReadDisposable = messageReader.markAsRead(id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe({}, { logger.error(it) })
    }
    /**
     * пересылка сообщения
     */
    fun onResendBtnClicked() {
        val message = currentMessageProvider.message ?: return

        val contacts = mutableMapOf<String, Contact>()
        message.recipients.associateTo(contacts) {
            Pair(it.id, Contact().apply {
                id = it.id
                name = it.name
            })
        }

        creatingMessageProvider.creatingMessage = CreatingMessage().apply {
            this.text = message.text
            this.contacts = contacts
        }

        view.navigateToResendMessage()
    }
}