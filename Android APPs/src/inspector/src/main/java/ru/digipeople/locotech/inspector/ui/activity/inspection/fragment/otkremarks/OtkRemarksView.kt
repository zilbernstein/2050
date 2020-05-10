package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks

import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter.WorkData
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс замечаний ОТК
 *
 * @author Kashonkov Nikita
 */
interface OtkRemarksView : MvpView {
    /**
     * установка данных
     */
    fun setData(adapterData: AdapterData?)
    /**
     * установка числа замечаний
     */
    fun setRemarkCount(count: Int)
    /**
     * обновить замечание
     */
    fun updateRemark(position: Int)
    /**
     * обновить работу
     */
    fun updateWork(workData: WorkData, position: Int)
    /**
     * управление видимостью кнопки
     */
    fun setCreateButtonVisibility(isVisible: Boolean)
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображние ошибки
     */
    fun showError(message: String)
}