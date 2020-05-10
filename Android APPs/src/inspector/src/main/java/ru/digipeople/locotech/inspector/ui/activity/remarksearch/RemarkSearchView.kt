package ru.digipeople.locotech.inspector.ui.activity.remarksearch

import ru.digipeople.locotech.inspector.model.RemarkFromCatalog
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс добавления/выбора замечаний
 * @author Kashonkov Nikita
 */
interface RemarkSearchView : MvpView {
    /**
     * обовление данных в адаптере
     */
    fun updateAdapter(list: List<RemarkFromCatalog>)
    /**
     * отмен диалога создания замечания
     */
    fun dismissCustomRemarkDialog()
    /**
     * дилог создания нового замечания
     */
    fun showCustomRemarkDialog(text: String)
    /**
     * сообщение что замечание уже добавлено
     */
    fun showRemarkAlreadyAddedError()
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisible(visible: Boolean)
    /**
     * отображние ошибки
     */
    fun showError(message: String)

    fun showEmptyNameError()
}