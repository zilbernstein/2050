package ru.digipeople.locotech.inspector.ui.activity.summary

import ru.digipeople.locotech.inspector.ui.activity.summary.adapter.SummaryItem
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * View State суммарной информации
 *
 * @author Kashonkov Nikita
 */
class SummaryViewState @Inject constructor() : BaseMvpViewState<SummaryView>(), SummaryView {
    override fun onViewAttached(view: SummaryView) {}

    override fun onViewDetached(view: SummaryView) {}
    /**
     * установка заголовка
     */
    override fun setTitle(equipmentName: String) {
        forEachView { it.setTitle(equipmentName) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun showLoading(isVisible: Boolean) {
        forEachView { it.showLoading(isVisible) }
    }
    /**
     * отображние загрузки
     */
    override fun showError(error: UserError) {
        forEachView { it.showError(error) }
    }
    /**
     * установка данных
     */
    override fun setData(summaryItemList: List<SummaryItem>) {
        forEachView { it.setData(summaryItemList) }
    }
}