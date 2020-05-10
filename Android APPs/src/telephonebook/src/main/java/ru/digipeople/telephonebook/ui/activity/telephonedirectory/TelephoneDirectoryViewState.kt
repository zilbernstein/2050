package ru.digipeople.telephonebook.ui.activity.telephonedirectory

import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState для телефонного справочника
 *
 * @author Sostavkin Grisha
 */
class TelephoneDirectoryViewState @Inject constructor() : BaseMvpViewState<TelephoneDirectoryView>(), TelephoneDirectoryView {

    override fun onViewAttached(view: TelephoneDirectoryView?) {}

    override fun onViewDetached(view: TelephoneDirectoryView?) {}
    /**
     * установка данных в адаптер
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
     * отобрабражение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
}