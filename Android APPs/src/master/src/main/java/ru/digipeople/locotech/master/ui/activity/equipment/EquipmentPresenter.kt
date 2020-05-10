package ru.digipeople.locotech.master.ui.activity.equipment

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.digipeople.locotech.core.data.model.EquipmentType
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.master.helper.session.EquipmentFilter
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.locotech.master.interactor.SelectEquipmentInteractor
import ru.digipeople.locotech.master.model.Equipment
import ru.digipeople.locotech.master.model.RepairType
import ru.digipeople.locotech.master.model.Section
import ru.digipeople.locotech.master.ui.activity.AppNavigator
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.DividerItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.EquipmentItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.LineEquipmentItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.SectionItem
import ru.digipeople.locotech.master.ui.activity.equipment.interactor.EquipmentLoader
import ru.digipeople.locotech.master.ui.activity.equipment.model.Tab
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.dialog.repairtype.RepairTypeViewModel
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Презентер локомотивов на учатске
 *
 * @author Kashonkov Nikita
 */
class EquipmentPresenter @Inject constructor(
        state: EquipmentViewState,
        private val subscriptionProvider: SubscriptionProvider,
        private val selectEquipmentInteractor: SelectEquipmentInteractor,
        private val equipmentLoader: EquipmentLoader,
        private val sessionManager: SessionManager,
        private val appNavigator: AppNavigator
) : BaseMvpViewStatePresenter<EquipmentView, EquipmentViewState>(state) {
    private val logger = LoggerFactory.getLogger(EquipmentPresenter::class)
    //region Other
    private val repairTypes = mutableSetOf<RepairTypeViewModel>()
    private var currentEquipment: ShortEquipment? = null
    private var selectEquipmentJob: Job = Job()
    private var equipments = emptyList<Equipment>()
    private var adapterData: AdapterData? = null
    private var activeTab: Tab = Tab.ALL

    private var subscriptionDisposable = Disposables.disposed()
    private var loadEquipmentDisposable = Disposables.disposed()
    private var filterDisposable = Disposables.disposed()
    //endRegion
    /**
     * операции при инициализации
     */
    override fun onInitialize() {
        currentEquipment = sessionManager.selectedEquipment
    }
    /**
     * уничтожение
     */
    override fun destroy() {
        subscriptionDisposable.dispose()
        loadEquipmentDisposable.dispose()
        filterDisposable.dispose()
        selectEquipmentJob.cancel()
        super.destroy()
    }
    /**
     * обработка нажатия на секцию
     */
    fun onSectionClicked(section: Section) {
        selectEquipment(section.id)
    }
    /**
     * обработка нажатия на линейное оборудование
     */
    fun onLineEquipmentClicked(equipment: Equipment) {
        selectEquipment(equipment.id)
    }
    /**
     * действия пр загрузке экрана
     */
    fun onScreenShown() {
        loadEquipment()
        subscribeToEquipmentSubscription()
    }
    /**
     * операции при уничтожении презентера
     */
    fun onDestroy() {
        subscriptionDisposable.dispose()
        loadEquipmentDisposable.dispose()
    }
    /**
     * действия при нажатии на кнопку фильтра по типу ремонта
     */
    fun onRepTypeFilterMenuItemClicked() {
        val (filterRepairTypes) = sessionManager.equipmentFilter
        val selectedRepairTypes = filterRepairTypes.map { RepairTypeViewModel(it.id, it.name) }
        view.showRepairTypesDialog(repairTypes, selectedRepairTypes)
    }
    /**
     * переключение вкладок
     */
    fun onTabClicked(tab: Tab) {
        if (tab != activeTab) {
            activeTab = tab
            filterAdapterData(sessionManager.equipmentFilter)
            view.setActiveTab(tab)
        }
    }
    /**
     * выбор значения для фильтра по типу ремонта
     */
    fun onRepairTypesSelected(repairTypes: List<RepairTypeViewModel>) {
        val filter = sessionManager.equipmentFilter
        val filterRepairTypes = repairTypes.map {
            RepairType().apply {
                id = it.id
                name = it.name
            }
        }
        filterAdapterData(filter.copy(repairTypes = filterRepairTypes))
    }
    /**
     * загрузка данных локомотивов и секций
     */
    private fun loadEquipment(shouldSavePosition: Boolean = false) {
        loadEquipmentDisposable.dispose()
        loadEquipmentDisposable = equipmentLoader.loadEquipment()
                .map { result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        equipments = result.equipments
                        selectRepairTypes(equipments)

                        val filter = sessionManager.equipmentFilter
                        Triple(result.userError, composeAdapterData(equipments, filter, shouldSavePosition),
                                resolvePerTabCount(equipments, filter))
                    } else Triple(result.userError, null, null)
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (userError, adapterData, perTabCount) ->
                    view.setLoadingVisibility(false)
                    this.adapterData = adapterData
                    if (userError == UserError.NO_ERROR) {
                        view.setPerTabCount(perTabCount!!)
                        view.setData(adapterData!!)
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * обработка выбора оборудования
     */
    private fun selectEquipment(equipmentId: String) {
        selectEquipmentJob.cancel()
        selectEquipmentJob = GlobalScope.launch(Dispatchers.Main) {
            val result = selectEquipmentInteractor.selectEquipment(equipmentId)
            /**
             * обработка результата
             */
            if (result.isSuccessful) {
                appNavigator.navigateToPerformance()
            } else {
                /**
                 * отображение ошибки
                 */
                view.showError(result.userError.message)
            }
        }
    }
    /**
     * сбор типов ремонта по секциям
     */
    private fun selectRepairTypes(equipments: List<Equipment>) {
        for (equipment in equipments) {
            equipment.sectionList.forEach { section ->
                section.repairType?.let {
                    repairTypes.add(RepairTypeViewModel(it.id, it.name))
                }
            }
        }
    }

    private fun subscribeToEquipmentSubscription() {
        subscriptionDisposable.dispose()
        subscriptionDisposable = subscriptionProvider.equipmentSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loadEquipment(true) }
    }
    /**
     * обработка сохранения фильтра
     */
    private fun filterAdapterData(filter: EquipmentFilter) {
        filterDisposable.dispose()
        filterDisposable = Single.fromCallable { Pair(composeAdapterData(equipments, filter, true), resolvePerTabCount(equipments, filter)) }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe { (adapterData, perTabCount) ->
                    view.setLoadingVisibility(false)
                    view.setPerTabCount(perTabCount)
                    view.setData(adapterData)
                }
        sessionManager.equipmentFilter = filter
    }
    /**
     * пересчет количесвта по вкладкам
     */
    private fun resolvePerTabCount(equipments: List<Equipment>, filter: EquipmentFilter): Map<Tab, Int> {
        val perTabCount = mutableMapOf(
                Tab.ALL to 0,
                Tab.WAITING to 0,
                Tab.IN_SERVICE to 0
        )
        /**
         * подсчет всего
         */
        var allCount = 0
        for (equipment in equipments) {
            filterSections(equipment, filter).forEach { item ->
                val tab = Tab.ofSectionState(item.state)
                perTabCount[tab] = (perTabCount[tab] ?: 0) + 1
                allCount++
            }
        }
        perTabCount[Tab.ALL] = allCount
        return perTabCount
    }
    /**
     * обновление данных на экране в соответствии со значениями фильтров
     */
    private fun composeAdapterData(equipments: List<Equipment>, filter: EquipmentFilter, shouldSavePosition: Boolean): AdapterData {
        val oldData = adapterData
        val newData = AdapterData()
        equipments.forEach { equipment ->
            if (equipment.type == EquipmentType.LINE_EQUIPMENT) {
                val lineEquipmentDataView = LineEquipmentItem(equipment)
                newData.add(lineEquipmentDataView)
                newData.add(DividerItem)
            } else {
                val filteredSections = filterSections(equipment, filter)

                val sectionsByTab = if (activeTab == Tab.ALL) {
                    filteredSections
                } else filteredSections.filter { Tab.ofSectionState(it.state) == activeTab }

                val shouldAddEquipment = sectionsByTab.isNotEmpty()
                if (shouldAddEquipment) {
                    val equipmentDataView = EquipmentItem(equipment)
                    newData.add(equipmentDataView)
                    val equipmentAdapterData = AdapterData()
                    sectionsByTab.forEach { section ->
                        section.equipmentHealth = section.equipmentHealth.sortedBy { it.position }
                        equipmentAdapterData.add(SectionItem(equipment, section))
                    }

                    equipmentDataView.sectionData = equipmentAdapterData
                    if (shouldSavePosition) {
                        val previousEquipmentDataView = oldData?.getEquipmentDataViewById(equipment)
                        if (previousEquipmentDataView != null) {
                            equipmentDataView.isExpanded = previousEquipmentDataView.isExpanded
                            if (equipmentDataView.isExpanded) {
                                newData.addAll(equipmentDataView.sectionData)
                            }
                        }
                    }
                    newData.add(DividerItem)
                }
            }
        }
        return newData
    }
    /**
     * фильтрация секций
     */
    private fun filterSections(equipment: Equipment, filter: EquipmentFilter): List<Section> {
        val (repairTypes) = filter
        return equipment.sectionList.filter { section ->
            repairTypes.isEmpty() || repairTypes.any { it.id == section.repairType?.id }
        }
    }
}