package ru.digipeople.locotech.master.ui.activity.equipment

import ru.digipeople.locotech.master.ui.activity.equipment.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.equipment.model.Tab
import ru.digipeople.ui.dialog.repairtype.RepairTypeViewModel
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс локомотивов на учатске
 * @author Kashonkov Nikita
 */
interface EquipmentView : MvpView {
    /**
     * установка данных
     */
    fun setData(adapterData: AdapterData)
    /**
     * отображение ошибки (без параметров)
     */
    fun showError()
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * установка выбранной вкладки
     */
    fun setActiveTab(tab: Tab)
    /**
     * отображение количества по вкладке
     */
    fun setPerTabCount(perTabCount: Map<Tab, Int>)
    /**
     * отображение диалога фильтрации по типу ремонта
     */
    fun showRepairTypesDialog(repairTypes: Set<RepairTypeViewModel>, selected: List<RepairTypeViewModel>)
}