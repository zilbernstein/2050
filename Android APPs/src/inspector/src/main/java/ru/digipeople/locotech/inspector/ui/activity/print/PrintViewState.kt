package ru.digipeople.locotech.inspector.ui.activity.print

import ru.digipeople.locotech.inspector.ui.activity.print.adapter.DocumentModel
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.SignersCategoryModel
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * View State печати
 *
 * @author Kashonkov Nikita
 */
class PrintViewState @Inject constructor() : BaseMvpViewState<PrintView>(), PrintView {

    private var documents: List<DocumentModel> = emptyList()
    private var signers: List<SignersCategoryModel> = emptyList()
    private var notifyDataSetChanged = false

    override fun onViewAttached(view: PrintView) {
        view.setDocuments(documents)
        if (notifyDataSetChanged) {
            view.notifyDataSetChanged()
            notifyDataSetChanged = false
        }
    }

    override fun onViewDetached(view: PrintView) {}
    /**
     * установить документы
     */
    override fun setDocuments(documents: List<DocumentModel>) {
        this.documents = documents
        forEachView { it.setDocuments(documents) }
    }
    /**
     * установить категории подписантов
     */
    override fun setSignersCategories(signersCategories: List<SignersCategoryModel>) {
        this.signers = signersCategories
        forEachView { it.setSignersCategories(signersCategories) }
    }
    /**
     * установить заголовок
     */
    override fun setTitle(equipmentName: String) {
        forEachView { it.setTitle(equipmentName) }
    }

    override fun notifyDataSetChanged() {
        notifyDataSetChanged = true
        forEachView {
            it.notifyDataSetChanged()
            notifyDataSetChanged = false
        }
    }
    /**
     * диалог удаления подписантов
     */
    override fun showDeleteSignersDialog() {
        forEachView { it.showDeleteSignersDialog() }
    }
    /**
     * диалог ввода почты
     */
    override fun showEnterEmailDialog(email: String) {
        forEachView { it.showEnterEmailDialog(email) }
    }
    /**
     * отображение ошибки
     */
    override fun showError(error: UserError) {
        forEachView { it.showError(error) }
    }
    /**
     * отображение сообщения
     */
    override fun showMessage(message: String) {
        forEachView { it.showMessage(message) }
    }

    override fun setTitleCheckAll(showClearAllTitle: Boolean) {
        forEachView { it.setTitleCheckAll(showClearAllTitle) }
    }

}