package ru.digipeople.message.ui.activity.messagedetail

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс детакли сообщения
 *
 * @author Kashonkov Nikita
 */
interface MessageDetailView: MvpView {
    /**
     * переход назад
     */
    fun navigateBack()
}