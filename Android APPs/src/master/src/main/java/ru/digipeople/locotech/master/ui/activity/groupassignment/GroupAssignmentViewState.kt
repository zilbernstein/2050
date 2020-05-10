package ru.digipeople.locotech.master.ui.activity.groupassignment

import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig.BrigadeAdapterData
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group.GroupAdapterData
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state группового назначения исполнителей
 *
 * @author Sostavkin Grisha
 */
class GroupAssignmentViewState @Inject constructor() : BaseMvpViewState<GroupAssignmentView>(), GroupAssignmentView {

    override fun onViewAttached(view: GroupAssignmentView?) {}

    override fun onViewDetached(view: GroupAssignmentView?) {}
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * загрузка данных в адаптер групп
     */
    override fun setDataToGroupAdapter(groupAdapterData: GroupAdapterData) {
        forEachView { it.setDataToGroupAdapter(groupAdapterData) }
    }
    /**
     * загрузка данных в адаптер бригад
     */
    override fun setDataToBrigAdapter(brigadeAdapterData: BrigadeAdapterData) {
        forEachView { it.setDataToBrigAdapter(brigadeAdapterData) }
    }
    /**
     * отображение ошибки назначения сотрудника из разных бригад
     */
    override fun showBrigadeError() {
        forEachView { it.showBrigadeError() }
    }
    /**
     * отображение ошибки маск числа исполнителей
     */
    override fun showMaxWorkersError() {
        forEachView { it.showMaxWorkersError() }
    }
    /**
     * Отображение ошибки недоступности рабочих
     */
    override fun showWorkerError() {
        forEachView { it.showWorkerError() }
    }
    /**
     * отображение ошибки не выбранной работы
     */
    override fun showWorkEmptyError() {
        forEachView { it.showWorkEmptyError() }
    }
    /**
     * Отображение ошибки не выбранного рабочего
     */
    override fun showWorkerEmptyError() {
        forEachView { it.showWorkerEmptyError() }
    }
    /**
     * отображение ошибки недоступности исполнителя
     */
    override fun showWorkersError() {
        forEachView { it.showWorkersError() }
    }
}