package ru.digipeople.locotech.master.ui.activity.remarksearch

import ru.digipeople.locotech.master.model.RemarkFromCatalog
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state добавления / создания замечания
 *
 * @author Kashonkov Nikita
 */
class RemarkSearchViewState @Inject constructor() : BaseMvpViewState<RemarkSearchView>(), RemarkSearchView {
    override fun onViewAttached(view: RemarkSearchView?) {}

    override fun onViewDetached(view: RemarkSearchView?) {}
    /**
     * Обновление данных в адаптере
     */
    override fun updateAdapter(list: List<RemarkFromCatalog>) {
        forEachView { it.updateAdapter(list) }
    }
    /**
     * Отмена создания нового замечания
     */
    override fun dismissCustomRemarkDialog() {
        forEachView { it.dismissCustomRemarkDialog() }
    }
    /**
     * Диалог создания нового замечания
     */
    override fun showCustomRemarkDialog(text: String) {
        forEachView { it.showCustomRemarkDialog(text) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        forEachView { it.setLoadingVisible(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * Отображение ошибки пустого наименования замечания
     */
    override fun showEmptyNameError() {
        forEachView { it.showEmptyNameError() }
    }
}