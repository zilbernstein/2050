package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks

import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.WorkData
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс цикловых работ
 *
 * @author Kashonkov Nikita
 */
interface CyclicWorksView : MvpView {
    /**
     * Установка данных
     */
    fun setData(data: AdapterData?)
    /**
     * Установка числа замечаний
     */
    fun setRemarkCount(count: Int)
    /**
     * обновление замечаний
     */
    fun updateRemark(position: Int)
    /**
     * Обновление работ
     */
    fun updateWork(workData: WorkData, position: Int)
    /**
     * Управленеи видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}