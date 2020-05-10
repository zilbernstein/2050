package ru.digipeople.message.ui.activity.writemessage

import ru.digipeople.message.model.Contact
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс отправки сообщения
 *
 * @author Kashonkov Nikita
 */
interface WriteMessageView : MvpView {
    /**
     * Переход назад
     */
    fun navigateBack()
    /**
     * переход к поиску адресатов
     */
    fun navigateToSearchAddressee()
    /**
     * установка текста
     */
    fun setText(text: String)
    /**
     * установка адресатов
     */
    fun setAddressees(addressees: List<Contact>)
    /**
     * сообщение о пустом списке адресатов
     */
    fun showEmptyAddresseeMistake()
    /**
     * Сообщение о пустом сообщении
     */
    fun showEmptyTextMistake()
    /**
     * отображение ошибки
     */
    fun showError(userError: UserError)
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
}