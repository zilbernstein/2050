package ru.digipeople.message.ui.fragment.messagelist

import ru.digipeople.message.model.Message
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс для фрагмента списка сообщений
 */
interface MessageListView : MvpView {
    /**
     * загрузка данных в адаптер
     */
    fun setDataToAdapter(list: List<Message>)
    /**
     * обработка нажатия на сообщение
     */
    fun invokeMessageClickListener(message: Message)
    /**
     * отображение сообщения
     */
    fun showError(userError: UserError)
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisible(visible: Boolean)
}