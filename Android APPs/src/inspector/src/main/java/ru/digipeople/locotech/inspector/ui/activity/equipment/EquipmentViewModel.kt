package ru.digipeople.locotech.inspector.ui.activity.equipment

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.data.model.EquipmentType
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.inspector.model.Equipment
import ru.digipeople.locotech.inspector.model.RepairType
import ru.digipeople.locotech.inspector.model.Section
import ru.digipeople.locotech.inspector.support.EquipmentFilter
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.DividerItem
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.EquipmentItem
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.SectionItem
import ru.digipeople.locotech.inspector.ui.activity.equipment.interactor.EquipmentLoader
import ru.digipeople.locotech.inspector.ui.activity.equipment.interactor.EquipmentWorker
import ru.digipeople.locotech.inspector.ui.activity.equipment.model.Tab
import ru.digipeople.locotech.inspector.ui.dialog.RepairTypeViewModel
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewModel локомотивов
 */
typealias RepairTypesFilterParams = Pair<Set<RepairTypeViewModel>, List<RepairTypeViewModel>>

class EquipmentViewModel @Inject constructor(
        private val sessionManager: SessionManager,
        private val subscriptionProvider: SubscriptionProvider,
        private val equipmentWorker: EquipmentWorker,
        private val equipmentLoader: EquipmentLoader,
        private val navigator: Navigator
) : BaseViewModel() {
    //region LiveData
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val equipmentsLiveData = MutableLiveData<AdapterData>()
    val perTabCountLiveData = MutableLiveData<Map<Tab, Int>>()
    val repairTypesFilterDialogLiveData = SingleEventLiveData<RepairTypesFilterParams>()
    val activeTabLiveData = MutableLiveData<Tab>()
    //endregion

    private var equipments = emptyList<Equipment>()
    private val repairTypes = mutableSetOf<RepairTypeViewModel>()
    private var activeTab: Tab = Tab.ALL
    private var subscriptionDisposable = Disposables.disposed()
    private var loadEquipmentDisposable = Disposables.disposed()
    private var selectEquipmentDisposable = Disposables.disposed()
    private var filterDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        subscribeToEquipmentSubscription()
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        subscriptionDisposable.dispose()
        loadEquipmentDisposable.dispose()
        selectEquipmentDisposable.dispose()
        filterDisposable.dispose()
    }
    /**
     * Обработка нажатия на секцию
     */
    fun onSectionClicked(equipment: Equipment, section: Section) {
        selectSection(equipment, section)
    }
    /**
     * Обработка нажатия на линейное оборудование
     */
    fun onLineEquipmentClicked(equipment: Equipment) {
        selectEquipment(equipment)
    }

    fun onScreenShown() {
        loadEquipment()
        activeTabLiveData.value = activeTab
    }
    /**
     * Обрабока нажатия на выбор типа ремонта
     */
    fun onRepTypeFilterMenuItemClicked() {
        val (filterRepairTypes) = sessionManager.equipmentFilter
        val selectedRepairTypes = filterRepairTypes.map { RepairTypeViewModel(it.id, it.name) }
        repairTypesFilterDialogLiveData.value = RepairTypesFilterParams(repairTypes, selectedRepairTypes)
    }
    /**
     * выбор вкладки
     */
    fun onTabClicked(tab: Tab) {
        if (tab != activeTab) {
            activeTab = tab

            filterAdapterData(sessionManager.equipmentFilter)
            activeTabLiveData.value = tab
        }
    }
    /**
     * Выбор типа ремонта
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
     * Загрузка оборудования
     */
    private fun loadEquipment() {
        loadEquipmentDisposable = equipmentLoader.loadEquipment()
                .map { result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        this.equipments = result.equipments
                        selectRepairTypes(equipments)

                        val filter = sessionManager.equipmentFilter
                        Triple(result.userError, composeAdapterData(result.equipments, filter),
                                resolvePerTabCount(result.equipments, filter))
                    } else Triple(result.userError, null, null)
                }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ (userError, adapterData, perTabCount) ->
                    loadingLiveData.value = false
                    /**
                     * Обработка типа ошибки
                     */
                    if (userError == UserError.NO_ERROR) {
                        equipmentsLiveData.value = adapterData
                        perTabCountLiveData.value = perTabCount
                    } else userErrorLiveData.value = userError
                }, { logger.error(it) })
    }
    /**
     * Выбоор секции
     */
    private fun selectSection(equipment: Equipment, section: Section) {
        selectEquipmentDisposable.dispose()
        selectEquipmentDisposable = equipmentWorker.selectSection(section.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe(
                        { result ->
                            /**
                             * Обработка результата
                             */
                            if (result.isSuccessful) {
                                sessionManager.updateSelectedEquipment(
                                        ShortEquipment().apply {
                                            id = equipment.id
                                            name = equipment.name
                                            type = equipment.type
                                        })
                                navigator.navigateToInspectionActivity()
                            } else {
                                /**
                                 * Обработка ошибки
                                 */
                                userErrorLiveData.value = result.userError
                            }
                        }, { logger.error(it) })
    }
    /**
     * Выбор оборудования
     */
    private fun selectEquipment(equipment: Equipment) {
        selectEquipmentDisposable.dispose()
        selectEquipmentDisposable = equipmentWorker.selectEquipment(equipment.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe(
                        { result ->
                            /**
                             * Обработка результата
                             */
                            if (result.isSuccessful) {
                                navigator.navigateToInspectionActivity()
                            } else {
                                /**
                                 * Отображение ошибки
                                 */
                                userErrorLiveData.value = result.userError
                            }
                        }, { logger.error(it) })
    }

    private fun subscribeToEquipmentSubscription() {
        subscriptionDisposable.dispose()
        subscriptionDisposable = subscriptionProvider.equipmentSubscription()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe({ logger.info("event was received") }, { logger.error(it) })
    }
    /**
     * Фильтрация данных
     */
    private fun filterAdapterData(filter: EquipmentFilter) {
        filterDisposable.dispose()
        filterDisposable = Single.fromCallable { Pair(composeAdapterData(equipments, filter), resolvePerTabCount(equipments, filter)) }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe { (adapterData, perTabCount) ->
                    loadingLiveData.value = false
                    perTabCountLiveData.value = perTabCount
                    equipmentsLiveData.value = adapterData
                }
        sessionManager.equipmentFilter = filter
    }
    /**
     * Выбор типа ремонта
     */
    private fun selectRepairTypes(equipments: List<Equipment>) {
        for (equipment in equipments) {
            equipment.sections.forEach { section ->
                section.repairType?.let {
                    repairTypes.add(RepairTypeViewModel(it.id, it.name))
                }
            }
        }
    }
    /**
     * Подсчет числа по пкладкам
     */
    private fun resolvePerTabCount(equipments: List<Equipment>, filter: EquipmentFilter): Map<Tab, Int> {
        val perTabCount = mutableMapOf(
                Tab.ALL to 0,
                Tab.WAITING to 0,
                Tab.IN_SERVICE to 0
        )

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

    private fun composeAdapterData(equipments: List<Equipment>, filter: EquipmentFilter): AdapterData {
        val newData = AdapterData()
        equipments.forEach { equipment ->
            if (equipment.type == EquipmentType.LINE_EQUIPMENT) {
                newData.add(equipment)
                newData.add(DividerItem)
            } else {
                val filteredSections = filterSections(equipment, filter)

                val sectionsByTab = if (activeTab == Tab.ALL) {
                    filteredSections
                } else filteredSections.filter { Tab.ofSectionState(it.state) == activeTab }

                val shouldAddEquipment = sectionsByTab.isNotEmpty()
                if (shouldAddEquipment) {
                    val equipmentDataView = EquipmentItem(equipment)
                    equipmentDataView.sections = sectionsByTab.map { section ->
                        section.equipmentHealth = section.equipmentHealth.sortedBy { it.position }
                        SectionItem(section, equipment)
                    }

                    newData.add(equipmentDataView)
                    newData.add(DividerItem)
                }
            }
        }
        return newData
    }

    private fun filterSections(equipment: Equipment, filter: EquipmentFilter): List<Section> {
        val (repairTypes) = filter
        return equipment.sections.filter { section ->
            repairTypes.isEmpty() || repairTypes.any { it.id == section.repairType?.id }
        }
    }
}