package ru.digipeople.telephonebook.ui.activity.telephone

import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс структуры телефонный справочник
 *
 * @author Sostavkin Grisha
 */
interface TelephoneBookView : MvpView {
    /**
     * загрузка данных в адаптер
     */
    fun setDataToAdapter(list: List<Contact>)
    /**
     * управление видимостью лоадера
     */
    fun setLoading(isLoading: Boolean)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    fun notifyVolxDataChanged()
    /**
     * проверка разрешения
     */
    fun checkPermissions()
}