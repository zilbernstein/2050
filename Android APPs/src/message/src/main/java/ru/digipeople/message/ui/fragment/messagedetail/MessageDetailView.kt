package ru.digipeople.message.ui.fragment.messagedetail

import ru.digipeople.message.model.Message
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс для фрагмента деталки сообщения
 */
interface MessageDetailView : MvpView {
    /**
     * показать сообщение
     */
    fun showMessage(message: Message?)
    /**
     * переход к пересылке сообщения
     */
    fun navigateToResendMessage()
}