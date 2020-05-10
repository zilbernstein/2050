package ru.digipeople.locotech.master.ui.activity.remarksearch

import ru.digipeople.locotech.master.model.RemarkFromCatalog
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс добавления / создания замечания
 *
 * @author Kashonkov Nikita
 */
interface RemarkSearchView: MvpView {
    /**
     * Обновление данных в адаптере
     */
    fun updateAdapter(list: List<RemarkFromCatalog>)
    /**
     * Закрытие диалого создания нового замечания
     */
    fun dismissCustomRemarkDialog()
    /**
     * Отображение диалога создания нового замечания
     */
    fun showCustomRemarkDialog(text: String)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Отображение ошибки отстутствия наименования замечания
     */
    fun showEmptyNameError()
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisible(isVisible: Boolean)
}