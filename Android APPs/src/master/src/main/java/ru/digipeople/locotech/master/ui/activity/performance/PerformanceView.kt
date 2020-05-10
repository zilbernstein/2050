package ru.digipeople.locotech.master.ui.activity.performance

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.performance.model.Tab
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс исполнения
 *
 * @author Kashonkov Nikita
 */
interface PerformanceView : MvpView {
    /**
     * выбор вкладки
     */
    fun setActiveTab(tab: Tab)
    /**
     * установка наименования оборудования
     */
    fun setEquipmentName(equipmentName: String)
    /**
     * загрузка данных в адаптер
     */
    fun setDataToAdapter(works: List<Work>)
    /**
     * установка числа работ во вкладке
     */
    fun setPerTabCount(perTabCount: Map<Tab, Int>)
    /**
     * установка наименования для кнопки Запустить все
     */
    fun setActionButtonForNewWork()
    /**
     * установка наименования для кнопки принять все
     */
    fun setActionButtonForDoneWork()
    /**
     * скрыть кнопку
     */
    fun hideActionButton()
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображение ошибки
     */
    fun showError(userError: UserError)
    /**
     * ошибка нет исполнителя
     */
    fun showNoPerformanceError()
    /**
     * ошибка пустого списка
     */
    fun showEmptyStartListError()
    /**
     * ошибка нельзя редактировать исполнителя при работе
     */
    fun showEditPerformersError()
    /**
     * ошибка при попытке принять пустой список работ
     */
    fun showAcceptAllError()
    /**
     * установка фильтар ТМЦ
     */

    fun setTmcFilterChecked(checked: Boolean)
    /**
     * установка фильтра Замеры
     */
    fun setMeasurementsFilterChecked(checked: Boolean)
    /**
     * установка фильтра МПИ
     */
    fun setMpiFilterChecked(checked: Boolean)
}