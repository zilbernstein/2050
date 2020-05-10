package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks

import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.WorkData
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
interface RzdRemarksView : MvpView {
    /**
     * установка данных
     */
    fun setData(adapterData: AdapterData?)
    /**
     * установка числа замечаний
     */
    fun setRemarkCount(count: Int)
    /**
     * обновление змечания
     */
    fun updateRemark(position: Int)
    /**
     * обновление работы
     */
    fun updateWork(workData: WorkData, position: Int)
    /**
     * управление видимостью кнопки
     */
    fun setCreateButtonVisibility(isVisible: Boolean)
    /**
     * управлени видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
}