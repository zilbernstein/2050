package ru.digipeople.locotech.master.ui.activity.searchperformer

import ru.digipeople.locotech.master.model.PerformerItem
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
class SearchPerformerViewState @Inject constructor() : BaseMvpViewState<SearchPerformerView>(), SearchPerformerView {
    //region Other
    private var workModel: Work? = null
    private var workers: List<PerformerItem>? = null
    private var workersInWork: List<PerformerItem>? = null
    //end Region
    override fun onViewAttached(view: SearchPerformerView) {
        workModel?.let { view.showWorkModel(it) }
        workers?.let { view.setWorkers(it) }
        workersInWork?.let { view.setWorkersInWork(it) }
    }

    override fun onViewDetached(view: SearchPerformerView?) {}
    /**
     * Выбор исполниетелей
     */
    override fun setWorkers(list: List<PerformerItem>) {
        workers = list
        forEachView { it.setWorkers(list) }
    }
    /**
     * Установка исполниетелей в работу
     */
    override fun setWorkersInWork(list: List<PerformerItem>) {
        workersInWork = list
        forEachView { it.setWorkersInWork(list) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }

    override fun showNullShiftError() {
        forEachView { it.showNullShiftError() }
    }
    /**
     * Отображение ошибки перегрузки исполнителя
     */
    override fun showOverLoadError() {
        forEachView { it.showOverLoadError() }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * Отображение данных
     */
    override fun showWorkModel(work: Work) {
        workModel = work
        forEachView { it.showWorkModel(work) }
    }
}