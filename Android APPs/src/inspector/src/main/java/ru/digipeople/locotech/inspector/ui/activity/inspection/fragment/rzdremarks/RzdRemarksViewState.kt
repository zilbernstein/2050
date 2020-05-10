package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks

import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.WorkData
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View State замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
class RzdRemarksViewState @Inject constructor() : BaseMvpViewState<RzdRemarksView>(), RzdRemarksView {
    var adapterData: AdapterData? = null
    private var isLoadingVisible = false
    var isCreateButtonVisible = false
    var count = 0

    override fun onViewAttached(view: RzdRemarksView) {
        view.setData(adapterData)
        view.setRemarkCount(count)
        view.setLoadingVisibility(isLoadingVisible)
        view.setCreateButtonVisibility(isCreateButtonVisible)
    }

    override fun onViewDetached(view: RzdRemarksView) {}
    /**
     * установка данных
     */
    override fun setData(adapterData: AdapterData?) {
        this.adapterData = adapterData
        forEachView { it.setData(adapterData) }
    }
    /**
     * установить число замечаний
     */
    override fun setRemarkCount(count: Int) {
        this.count = count
        forEachView { it.setRemarkCount(count) }
    }
    /**
     * обновление замечания
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
     * отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * управление видимосьтю лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
}