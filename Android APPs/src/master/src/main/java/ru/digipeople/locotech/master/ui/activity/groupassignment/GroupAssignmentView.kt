package ru.digipeople.locotech.master.ui.activity.groupassignment

import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig.BrigadeAdapterData
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group.GroupAdapterData
import ru.digipeople.ui.mvp.view.MvpView

/**Интерфейс группового назначения исполнителей
 *
 * @author Sostavkin Grisha
 */
interface GroupAssignmentView : MvpView {
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    /**
     * отображение ошибки назначения сотрудника из разных бригад
     */
    fun showBrigadeError()
    /**
     * загрузка данных в адаптер групп
     */
    fun setDataToGroupAdapter(groupAdapterData: GroupAdapterData)
    /**
     * загрузка данных в адаптер бригад
     */
    fun setDataToBrigAdapter(brigadeAdapterData: BrigadeAdapterData)
    /**
     * отображение ошибки маск числа исполнителей
     */
    fun showMaxWorkersError()
    /**
     * Отображение ошибки недоступности рабочих
     */
    fun showWorkerError()
    /**
     * отображение ошибки не выбранной работы
     */
    fun showWorkEmptyError()
    /**
     * Отображение ошибки не выбранного рабочего
     */
    fun showWorkerEmptyError()
    /**
     * отображение ошибки недоступности исполнителя
     */
    fun showWorkersError()
}