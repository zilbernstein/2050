package ru.digipeople.message.ui.activity.messagelist

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс списка сообщений
 *
 * @author Kashonkov Nikita
 */
interface MessageListView : MvpView {
    /**
     * Переход к деталке сообщения
     */
    fun navigateToMessageDetail()
    /**
     * переход к написанию нового сообщения
     */
    fun navigateToWriteMessage()
}