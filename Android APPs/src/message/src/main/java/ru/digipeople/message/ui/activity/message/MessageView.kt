package ru.digipeople.message.ui.activity.message

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс сообщения
 *
 * @author Kashonkov Nikita
 */
interface MessageView : MvpView {
    /**
     * Переход к новому сообщению
     */
    fun navigateToNewMessage()
}