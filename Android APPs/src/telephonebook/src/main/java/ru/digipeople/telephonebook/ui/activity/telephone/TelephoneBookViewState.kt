package ru.digipeople.telephonebook.ui.activity.telephone

import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState телефонный справочник
 *
 * @author Sostavkin Grisha
 */
class TelephoneBookViewState @Inject constructor() : BaseMvpViewState<TelephoneBookView>(), TelephoneBookView {
    override fun onViewAttached(view: TelephoneBookView?) {
    }

    override fun onViewDetached(view: TelephoneBookView?) {}
    /**
     * загрузка жданных в адаптер
     */
    override fun setDataToAdapter(list: List<Contact>) {
        forEachView { it.setDataToAdapter(list) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoading(isLoading: Boolean) {
        forEachView { it.setLoading(isLoading) }
    }
    /**
     * проверка разрешения
     */
    override fun checkPermissions() {
        forEachView { it.checkPermissions() }
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }

    override fun notifyVolxDataChanged() {
        forEachView { it.notifyVolxDataChanged() }
    }
}