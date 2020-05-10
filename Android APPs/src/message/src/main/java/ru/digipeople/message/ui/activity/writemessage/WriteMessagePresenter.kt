package ru.digipeople.message.ui.activity.writemessage

import io.reactivex.disposables.Disposables
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.message.helper.CreatingMessageProvider
import ru.digipeople.message.model.Contact
import ru.digipeople.message.model.CreatingMessage
import ru.digipeople.message.ui.activity.writemessage.interactor.MessageSender
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер отправки сообщения
 */
class WriteMessagePresenter @Inject constructor(viewState: WriteMessageViewState,
                                                private val messageSender: MessageSender,
                                                private val creatingMessageProvider: CreatingMessageProvider) : BaseMvpViewStatePresenter<WriteMessageView, WriteMessageViewState>(viewState) {

    /**
     * подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(WriteMessagePresenter::class)

    private lateinit var message: CreatingMessage
    private var sendDisposable = Disposables.disposed()
    /**
     * Действия при иниуиализацие
     */
    override fun onInitialize() {
        message = creatingMessageProvider.creatingMessage ?: CreatingMessage()
        creatingMessageProvider.creatingMessage = message
        view.setText(message.text)
    }

    override fun destroy() {
        super.destroy()
        creatingMessageProvider.creatingMessage = null
    }

    fun onScreenShown() {
        view.setAddressees(message.contacts.values.toList())
    }
    /**
     * Обрабюотка добавления адресата
     */
    fun onAddresseeImageClicked() {
        view.navigateToSearchAddressee()
    }
    /**
     * Обработка отправки сообщения
     */
    fun onSendButtonClicked(text: String) {
        message.text = text
        sendMessage()
    }
    /**
     * Обработка нажатия удалить адресата
     */
    fun onDeleteAddresseeButtonClicked(contact: Contact) {
        message.contacts.remove(contact.id)
        view.setAddressees(message.contacts.values.toList())
    }
    /**
     * Отправка сообщения
     */
    private fun sendMessage() {
        if (!checkData()) {
            return
        } else {
            sendDisposable.dispose()
            sendDisposable = messageSender.sendMessage(message.contacts.keys, message.text)
                    .subscribeOn(AppSchedulers.io())
                    .observeOn(AppSchedulers.ui())
                    .doOnSubscribe { view.setLoadingVisibility(true) }
                    .subscribe({ result ->
                        /**
                         * обработка результата
                         */
                        view.setLoadingVisibility(false)
                        if (result.isSuccessful) {
                            view.navigateBack()
                        } else {
                            /**
                             * отображение ошибки
                             */
                            view.showError(result.userError)
                        }
                    }, { logger.error(it) })
        }
    }
    /**
     * проверка заполненности полей
     */
    private fun checkData(): Boolean {
        if (message.contacts.isEmpty()) {
            view.showEmptyAddresseeMistake()
            return false
        } else if (message.text.isEmpty()) {
            view.showEmptyTextMistake()
            return false
        }
        return true
    }
}