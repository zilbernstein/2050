package ru.digipeople.message.ui.activity.writemessage

import ru.digipeople.message.model.Contact
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewState отправки сообщения
 */
class WriteMessageViewState @Inject constructor() : BaseMvpViewState<WriteMessageView>(), WriteMessageView {

    private var text: String? = null

    override fun onViewAttached(view: WriteMessageView) {
        text?.let {
            view.setText(it)
            text = null
        }
    }

    override fun onViewDetached(view: WriteMessageView) {}
    /**
     * переход назад
     */
    override fun navigateBack() {
        forEachView { it.navigateBack() }
    }
    /**
     * переход к поиску адресатов
     */
    override fun navigateToSearchAddressee() {
        forEachView { it.navigateToSearchAddressee() }
    }
    /**
     * сообщение о пустом списке адресатов
     */
    override fun showEmptyAddresseeMistake() {
        forEachView { it.showEmptyAddresseeMistake() }
    }
    /**
     * сообщене о пустом сообщении
     */
    override fun showEmptyTextMistake() {
        forEachView { it.showEmptyTextMistake() }
    }
    /**
     * установка теста
     */
    override fun setText(text: String) {
        this.text = text
        forEachView {
            it.setText(text)
            this.text = null
        }
    }
    /**
     * установка адресатов
     */
    override fun setAddressees(addressees: List<Contact>) {
        forEachView { it.setAddressees(addressees) }
    }
    /**
     * Сообщение об ошибке
     */
    override fun showError(userError: UserError) {
        forEachView { it.showError(userError) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
}