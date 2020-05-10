package ru.digipeople.locotech.master.ui.activity.tmclist

import ru.digipeople.locotech.master.model.TMCInWork
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state списка ТМЦ
 * @author Kashonkov Nikita
 */
class TmcListViewState @Inject constructor() : BaseMvpViewState<TmcListView>(), TmcListView {
    private var data: List<TMCInWork> = emptyList()
    private var loadinigVisibility = false

    override fun onViewAttached(view: TmcListView) {
        view.setData(data)
        view.setLoadingVisibility(loadinigVisibility)
    }

    override fun onViewDetached(view: TmcListView) {}
    /**
     * Установка данных
     */
    override fun setData(list: List<TMCInWork>) {
        this.data = list
        forEachView { it.setData(list) }
    }
    /**
     * Отображение диалога удаления
     */
    override fun showDeleteDialog() {
        forEachView { it.showDeleteDialog() }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        this.loadinigVisibility = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
}