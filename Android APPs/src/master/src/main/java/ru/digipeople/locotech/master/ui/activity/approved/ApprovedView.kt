package ru.digipeople.locotech.master.ui.activity.approved

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс согласование
 *
 * @author Kashonkov Nikita
 */
interface ApprovedView : MvpView {
    /**
     * установить данные о согласованных работах в адаптер
     */
    fun setDataToApprovedAdapter(list: List<Work>)
    /**
     * установить данные о несогласованных работах в адаптер
     */
    fun setDataToNorApprovedAdapter(list: List<Work>)
    /**
     * отображение ошибки согласования работы
     */
    fun showApproveError()
    /**
     * сообщение об ошибке
     */
    fun showError(message: String)
    /**
     * управление видимостью загрузки
     */
    fun setLoadingVisible(isVisible: Boolean)
}