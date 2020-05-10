package ru.digipeople.message.ui.fragment.messagelist

import io.reactivex.disposables.Disposables
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.message.helper.CurrentMessageProvider
import ru.digipeople.message.model.Message
import ru.digipeople.message.ui.fragment.messagelist.interactor.MessagesLoader
import ru.digipeople.message.ui.fragment.messagelist.model.Tab
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер фрагмента списка сообщений
 */
class MessageListPresenter @Inject constructor(viewState: MessageListViewState,
                                               private val currentMessageProvider: CurrentMessageProvider,
                                               private val messagesLoader: MessagesLoader) : BaseMvpViewStatePresenter<MessageListView, MessageListViewState>(viewState) {

    /**
     * подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(MessageListPresenter::class)

    private var incomeMessages = emptyList<Message>()
    private var outcomeMessages = emptyList<Message>()
    private var currentTab = Tab.OUTCOME
    private var loadMessagesDisposable = Disposables.disposed()

    override fun onInitialize() {
        // nop
    }

    override fun destroy() {
        loadMessagesDisposable.dispose()
        currentMessageProvider.message = null
    }
    /**
     * получение сообщений
     */
    fun onScreenShown() {
        loadMessagesDisposable.dispose()
        loadMessagesDisposable = messagesLoader.getMessages()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisible(false)
                    incomeMessages = result.incomeMessages
                    outcomeMessages = result.outcomeMessages
                    setMessagesToView()
                    /**
                     * обработка ошибки
                     */
                    if (!result.isSuccessful) {
                        view.showError(result.userError)
                    }
                }, { logger.error(it) })
    }
    /**
     * ннажатие на сообшение
     */
    fun onMessageClicked(message: Message) {
        currentMessageProvider.message = message
        view.invokeMessageClickListener(message)
    }
    /**
     * выбор вкладки
     */
    fun onTabSelected(tab: Tab) {
        currentTab = tab
        setMessagesToView()
    }
    /**
     * отрисовка сообщения в окне
     */
    private fun setMessagesToView() {
        if (currentTab == Tab.INCOME) {
            view.setDataToAdapter(incomeMessages)
        } else {
            view.setDataToAdapter(outcomeMessages)
        }
    }
}