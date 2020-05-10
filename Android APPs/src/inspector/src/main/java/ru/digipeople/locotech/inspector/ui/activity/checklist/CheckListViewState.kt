package ru.digipeople.locotech.inspector.ui.activity.checklist

import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.OperationData
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * Вью-стейт чек-лсита
 *
 * @author Kashonkov Nikita
 */
class CheckListViewState @Inject constructor() : BaseMvpViewState<CheckListView>(), CheckListView {
    private var data: AdapterData? = null
    private var title: String? = null
    private var isLoadingVisible = false
    private var isFinishBtnVisible = false

    override fun onViewAttached(view: CheckListView) {
        view.setData(data)
        view.setTitle(title)
        view.setLoadingVisibility(isLoadingVisible)
        view.setFinishBtnVisibility(isFinishBtnVisible)
    }

    override fun onViewDetached(view: CheckListView) {
    }
    /**
     * установка заголовка
     */
    override fun setTitle(equipmentName: String?) {
        title = equipmentName
        forEachView { it.setTitle(equipmentName) }
    }
    /**
     * установка данных
     */
    override fun setData(adapterData: AdapterData?) {
        this.data = adapterData
        forEachView { it.setData(adapterData) }
    }
    /**
     * обновление работы
     */
    override fun updateWork(operationData: OperationData, position: Int) {
        forEachView { it.updateWork(operationData, position) }
    }
    /**
     * показать диалог согласования
     */
    override fun showApproveDialog() {
        forEachView { it.showApproveDialog() }
    }

    override fun dismissApproveDialog() {
        forEachView { it.dismissApproveDialog() }
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        this.isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }

    override fun setFinishBtnVisibility(isVisibile: Boolean) {
        isFinishBtnVisible = isVisibile
        forEachView { it.setFinishBtnVisibility(isVisibile) }
    }
}