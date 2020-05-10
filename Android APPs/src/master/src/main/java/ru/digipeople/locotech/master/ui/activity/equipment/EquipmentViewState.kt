package ru.digipeople.locotech.master.ui.activity.equipment

import ru.digipeople.locotech.master.ui.activity.equipment.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.equipment.model.Tab
import ru.digipeople.ui.dialog.repairtype.RepairTypeViewModel
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state локомотивов на учатске
 *
 * @author Kashonkov Nikita
 */
class EquipmentViewState @Inject constructor() : BaseMvpViewState<EquipmentView>(), EquipmentView {
    private var isLoadingVisible = false
    private var activeTab = Tab.ALL

    override fun onViewAttached(view: EquipmentView) {
        view.setLoadingVisibility(isLoadingVisible)
        view.setActiveTab(activeTab)
    }

    override fun onViewDetached(view: EquipmentView?) {}
    /**
     * установка данных
     */
    override fun setData(adapterData: AdapterData) {
        forEachView { it.setData(adapterData) }
    }
    /**
     * отобюажение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * отображение ошибки ьез параметров
     */
    override fun showError() {
        forEachView { it.showError() }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * отображение ошибки
     */
    override fun setActiveTab(tab: Tab) {
        activeTab = tab
        forEachView { it.setActiveTab(tab) }
    }
    /**
     * установка кол-ва по вкладкам
     */
    override fun setPerTabCount(perTabCount: Map<Tab, Int>) {
        forEachView { it.setPerTabCount(perTabCount) }
    }
    /**
     * отображение диалога фильтра по типу ремонта
     */
    override fun showRepairTypesDialog(repairTypes: Set<RepairTypeViewModel>, selected: List<RepairTypeViewModel>) {
        forEachView { it.showRepairTypesDialog(repairTypes, selected) }
    }
}