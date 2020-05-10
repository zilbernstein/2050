package ru.digipeople.locotech.inspector.ui.activity.print

import ru.digipeople.locotech.inspector.ui.activity.print.adapter.DocumentModel
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.SignersCategoryModel
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс печати
 *
 * @author Kashonkov Nikita
 */
interface PrintView : MvpView {
    /**
     * установить документы
     */
    fun setDocuments(documents: List<DocumentModel>)
    /**
     * установить категории подписантов
     */
    fun setSignersCategories(signersCategories: List<SignersCategoryModel>)
    /**
     * установить заголовок
     */
    fun setTitle(equipmentName: String)
    fun notifyDataSetChanged()
    /**
     * диалог удаления подписантов
     */
    fun showDeleteSignersDialog()
    /**
     * диалог ввода почты
     */
    fun showEnterEmailDialog(email: String)
    /**
     * отображние ошибки
     */
    fun showError(error: UserError)
    /**
     * отображнеи сообщения
     */
    fun showMessage(message: String)
    /**
     * управенеи заголовком выбора всех
     */
    fun setTitleCheckAll(showClearAllTitle: Boolean)
}