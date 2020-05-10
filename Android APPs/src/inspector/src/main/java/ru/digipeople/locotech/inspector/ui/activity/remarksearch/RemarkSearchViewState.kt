package ru.digipeople.locotech.inspector.ui.activity.remarksearch

import ru.digipeople.locotech.inspector.model.RemarkFromCatalog
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View State добавления/выбора замечаний
 * @author Kashonkov Nikita
 */
class RemarkSearchViewState @Inject constructor() : BaseMvpViewState<RemarkSearchView>(), RemarkSearchView {
    override fun onViewAttached(view: RemarkSearchView?) {}

    override fun onViewDetached(view: RemarkSearchView?) {}

    override fun updateAdapter(list: List<RemarkFromCatalog>) {
        forEachView { it.updateAdapter(list) }
    }
    /**
     * отмена диалога нового замечания
     */
    override fun dismissCustomRemarkDialog() {
        forEachView { it.dismissCustomRemarkDialog() }
    }
    /**
     * диалог нового замечания
     */
    override fun showCustomRemarkDialog(text: String) {
        forEachView { it.showCustomRemarkDialog(text) }
    }
    /**
     * сообщение что замечание уже добавлено
     */
    override fun showRemarkAlreadyAddedError() {
        forEachView { it.showRemarkAlreadyAddedError() }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisible(visible: Boolean) {
        forEachView { it.setLoadingVisible(visible) }
    }
    /**
     * отображнеи ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }

    override fun showEmptyNameError() {
        forEachView { it.showEmptyNameError() }
    }
}