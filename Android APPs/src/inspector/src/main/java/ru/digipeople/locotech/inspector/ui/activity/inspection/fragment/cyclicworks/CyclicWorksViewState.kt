package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks

import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.WorkData
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View State цикловых работ
 * @author Kashonkov Nikita
 */
class CyclicWorksViewState @Inject constructor(): BaseMvpViewState<CyclicWorksView>(), CyclicWorksView {
    private var data: AdapterData? = null
    private var isLoadingVisible = false
    private var count  = 0

    override fun onViewAttached(view: CyclicWorksView) {
        view.setData(data)
        view.setRemarkCount(count)
        view.setLoadingVisibility(isLoadingVisible)
    }

    override fun onViewDetached(view: CyclicWorksView?) {}

    /**
     * Установка данных
     */
    override fun setData(data: AdapterData?) {
        this.data = data
        forEachView { it.setData(data) }
    }
    /**
     * Установка числа замечаний
     */
    override fun setRemarkCount(count: Int) {
        this.count = count
        forEachView { it.setRemarkCount(count) }
    }
    /**
     * Обновление замечаний
     */
    override fun updateRemark(position: Int) {
        forEachView { it.updateRemark(position) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * Обновление работы
     */
    override fun updateWork(workData: WorkData, position: Int) {
        forEachView { it.updateWork(workData, position) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
}