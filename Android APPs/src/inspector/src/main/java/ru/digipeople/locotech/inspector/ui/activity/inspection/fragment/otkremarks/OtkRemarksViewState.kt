package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks

import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter.WorkData
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View State замечаний ОТК
 * @author Kashonkov Nikita
 */
class OtkRemarksViewState @Inject constructor() : BaseMvpViewState<OtkRemarksView>(), OtkRemarksView {
    var adapterData: AdapterData? = null
    private var isLoadingVisible = false
    var isCreateButtonVisible = false
    var count = 0

    override fun onViewAttached(view: OtkRemarksView) {
        view.setData(adapterData)
        view.setRemarkCount(count)
        view.setLoadingVisibility(isLoadingVisible)
        view.setCreateButtonVisibility(isCreateButtonVisible)
    }

    override fun onViewDetached(view: OtkRemarksView?) {}
    /**
     * установка данных
     */
    override fun setData(adapterData: AdapterData?) {
        this.adapterData = adapterData
        forEachView { it.setData(adapterData) }
    }
    /**
     * установка числа замечаний
     */
    override fun setRemarkCount(count: Int) {
        this.count = count
        forEachView { it.setRemarkCount(count) }
    }
    /**
     * обновление замечаний
     */
    override fun updateRemark(position: Int) {
        forEachView { it.updateRemark(position) }
    }
    /**
     * обновление работы
     */
    override fun updateWork(workData: WorkData, position: Int) {
        forEachView { it.updateWork(workData, position) }
    }
    /**
     * управление видимостью кнопки
     */
    override fun setCreateButtonVisibility(isVisible: Boolean) {
        isCreateButtonVisible = isVisible
        forEachView { it.setCreateButtonVisibility(isVisible) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * отбраженеи ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
}