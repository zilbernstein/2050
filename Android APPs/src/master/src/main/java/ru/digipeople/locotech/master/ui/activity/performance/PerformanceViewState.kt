package ru.digipeople.locotech.master.ui.activity.performance

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.performance.model.Tab
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View state исполнения
 *
 * @author Kashonkov Nikita
 */
class PerformanceViewState @Inject constructor() : BaseMvpViewState<PerformanceView>(), PerformanceView {
    private var isLoadingVisible = false
    private var works: List<Work>? = null
    private var activeTab = Tab.IN_TASK

    override fun onViewAttached(view: PerformanceView) {
        view.setLoadingVisibility(isLoadingVisible)
        view.setActiveTab(activeTab)
        works?.let {
            view.setDataToAdapter(it)
            works = null
        }
    }

    override fun onViewDetached(view: PerformanceView?) {}

    override fun setActiveTab(tab: Tab) {
        this.activeTab = tab
        forEachView { it.setActiveTab(tab) }
    }
    /**
     * установка данных в адаптер
     */
    override fun setDataToAdapter(works: List<Work>) {
        this.works = works
        forEachView {
            it.setDataToAdapter(works)
            this.works = null
        }
    }
    /**
     * ошибка нельзя редактировать исполнителя при работе
     */
    override fun showEditPerformersError() {
        forEachView { it.showEditPerformersError() }
    }
    /**
     * ошибка при попытке принять пустой список работ
     */
    override fun showAcceptAllError() {
        forEachView { it.showAcceptAllError() }
    }
    /**
     * установка наименования оборуования
     */
    override fun setEquipmentName(equipmentName: String) {
        forEachView { it.setEquipmentName(equipmentName) }
    }
    /**
     * установка количества работ во вкладке
     */
    override fun setPerTabCount(perTabCount: Map<Tab, Int>) {
        forEachView { it.setPerTabCount(perTabCount) }
    }
    /**
     * установка наименования для кнопки Запустить все
     */
    override fun setActionButtonForNewWork() {
        forEachView { it.setActionButtonForNewWork() }
    }
    /**
     * установка наименования для кнопки принять все
     */
    override fun setActionButtonForDoneWork() {
        forEachView { it.setActionButtonForDoneWork() }
    }
    /**
     * скрыть кнопку
     */
    override fun hideActionButton() {
        forEachView { it.hideActionButton() }
    }
    /**
     * отображение ошибки
     */
    override fun showError(userError: UserError) {
        forEachView { it.showError(userError) }
    }
    /**
     * ошибка нет исполнителя
     */
    override fun showNoPerformanceError() {
        forEachView { it.showNoPerformanceError() }
    }
    /**
     * ошибка пустого списка
     */
    override fun showEmptyStartListError() {
        forEachView { it.showEmptyStartListError() }
    }
    /**
     * управлние видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * установка фильтра ТМЦ
     */
    override fun setTmcFilterChecked(checked: Boolean) {
        forEachView { it.setTmcFilterChecked(checked) }
    }
    /**
     * установка фильтра Замеры
     */
    override fun setMeasurementsFilterChecked(checked: Boolean) {
        forEachView { it.setMeasurementsFilterChecked(checked) }
    }
    /**
     * установка фильтра МПИ
     */
    override fun setMpiFilterChecked(checked: Boolean) {
        forEachView { it.setMpiFilterChecked(checked) }
    }
}