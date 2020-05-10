package ru.digipeople.message.ui.activity.chooseaddressee

import ru.digipeople.message.model.Contact
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерефейс для структуры выбора адресата
 */
interface ChooseAddresseeView : MvpView {
    /**
     * Установка данных
     */
    fun setData(contacts: List<Contact>)
    /**
     * переход на предыдущий экран
     */
    fun navigateBack()
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showUserError(userError: UserError)
}