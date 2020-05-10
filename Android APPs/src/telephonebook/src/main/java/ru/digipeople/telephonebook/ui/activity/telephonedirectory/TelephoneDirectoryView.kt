package ru.digipeople.telephonebook.ui.activity.telephonedirectory

import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс структуры телефонный справочник
 *
 * @author Sostavkin Grisha
 */
interface TelephoneDirectoryView : MvpView {
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
}