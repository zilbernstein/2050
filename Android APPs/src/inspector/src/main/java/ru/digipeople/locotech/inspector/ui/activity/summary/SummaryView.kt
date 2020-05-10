package ru.digipeople.locotech.inspector.ui.activity.summary

import ru.digipeople.locotech.inspector.ui.activity.summary.adapter.SummaryItem
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс суммарной информации
 *
 * @author Kashonkov Nikita
 */
interface SummaryView: MvpView {
    /**
     * установка данных
     */
    fun setTitle(equipmentName: String)
    /**
     * управлнеи видимостью лоадера
     */
    fun showLoading(isVisible: Boolean)
    /**
     * отображние ошибки
     */
    fun showError(error: UserError)
    /**
     * установка данных
     */
    fun setData(summaryItemList: List<SummaryItem>)
}