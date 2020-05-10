package ru.digipeople.locotech.master.ui.activity.approved

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state согласование
 *
 * @author Kashonkov Nikita
 */
class ApprovedViewState @Inject constructor() : BaseMvpViewState<ApprovedView>(), ApprovedView {
    override fun onViewAttached(view: ApprovedView?) {}

    override fun onViewDetached(view: ApprovedView?) {}
    /**
     * установить данные о согласованных работах в адаптер
     */
    override fun setDataToApprovedAdapter(list: List<Work>) {
        forEachView { it.setDataToApprovedAdapter(list) }
    }
    /**
     * установить данные о несогласованных работах в адаптер
     */
    override fun setDataToNorApprovedAdapter(list: List<Work>) {
        forEachView { it.setDataToNorApprovedAdapter(list) }
    }
    /**
     * отображение ошибки согласования работы
     */
    override fun showApproveError() {
        forEachView { it.showApproveError() }
    }
    /**
     * сообщение об ошибке
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * управление видимостью загрузки
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        forEachView { it.setLoadingVisible(isVisible) }
    }
}