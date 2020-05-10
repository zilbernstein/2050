package ru.digipeople.locotech.master.ui.activity.tmcsearch

import ru.digipeople.locotech.master.model.TMC
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * view state поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcSearchViewState @Inject constructor() : BaseMvpViewState<TmcSearchView>(), TmcSearchView {

    private var items: List<TMC> = emptyList()
    private var loadingVisible = false
    private var noDataMsgVisible = false
    private var headerVisible = false

    override fun onViewAttached(view: TmcSearchView) {
        view.setItems(items)
        view.setLoadingVisible(loadingVisible)
        view.setNoDataMsgVisible(noDataMsgVisible)
        view.setHeaderVisible(headerVisible)
    }

    override fun onViewDetached(view: TmcSearchView) {}
    /**
     * Установка данных
     */
    override fun setItems(items: List<TMC>) {
        this.items = items
        forEachView { it.setItems(items) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(visible: Boolean) {
        this.loadingVisible = visible
        forEachView { it.setLoadingVisible(visible) }
    }
    /**
     * Отображение сообщение об отсутствии данных
     */
    override fun setNoDataMsgVisible(visible: Boolean) {
        this.noDataMsgVisible = visible
        forEachView { it.setNoDataMsgVisible(visible) }
    }
    /**
     * Установка видимости хедера
     */
    override fun setHeaderVisible(visible: Boolean) {
        this.headerVisible = visible
        forEachView { it.setHeaderVisible(visible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(error: UserError) {
        forEachView { it.showError(error) }
    }
}