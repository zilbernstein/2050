package ru.digipeople.locotech.master.ui.activity.worklist

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state добавления замечания / работ
 *
 * @author Kashonkov Nikita
 */
class WorkListViewState @Inject constructor(): BaseMvpViewState<WorkListView>(), WorkListView {
    override fun onViewAttached(view: WorkListView?) {}

    override fun onViewDetached(view: WorkListView?) {}
    /**
     * Загрузка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Work>) { forEachView { it.setDataToAdapter(list) } }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) { forEachView { it.setLoadingVisible(isVisible) } }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) { forEachView { it.showError(message) } }
}