package ru.digipeople.message.ui.fragment.messagelist

import ru.digipeople.message.model.Message
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewState для фрагмента списка сообщений
 */
class MessageListViewState @Inject constructor() : BaseMvpViewState<MessageListView>(), MessageListView {

    override fun onViewAttached(view: MessageListView?) {}

    override fun onViewDetached(view: MessageListView?) {}
    /**
     * загрузка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Message>) {
        forEachView { it.setDataToAdapter(list) }
    }
    /**
     * обработка нажатия на сообщение
     */
    override fun invokeMessageClickListener(message: Message) {
        forEachView { it.invokeMessageClickListener(message) }
    }
    /**
     * отображение ошибки
     */
    override fun showError(userError: UserError) {
        forEachView { it.showError(userError) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisible(visible: Boolean) {
        forEachView { it.setLoadingVisible(visible) }
    }
}