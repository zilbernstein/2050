package ru.digipeople.locotech.metrologist.ui.activity.sections

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.data.model.Locomotive
import ru.digipeople.locotech.metrologist.data.model.RepairType
import ru.digipeople.locotech.metrologist.data.model.Section
import ru.digipeople.locotech.metrologist.helper.session.EquipmentFilter
import ru.digipeople.locotech.metrologist.helper.session.SessionManager
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.sections.interactor.SectionsLoader
import ru.digipeople.locotech.metrologist.ui.activity.sections.interactor.SelectSectionInteractor
import ru.digipeople.locotech.metrologist.ui.activity.sections.model.Tab
import ru.digipeople.ui.dialog.repairtype.RepairTypeViewModel
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * View model секций
 */
typealias RepairTypesFilterParams = Pair<Set<RepairTypeViewModel>, List<RepairTypeViewModel>>

class SectionsViewModel @Inject constructor(
        private val navigator: AppNavigator,
        private val sessionManager: SessionManager,
        private val sectionsLoader: SectionsLoader,
        private val selectSectionInteractor: SelectSectionInteractor
) : BaseViewModel() {
    //region LiveData
    val locomotivesLiveData = MutableLiveData<List<Locomotive>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val activeTabLiveData = MutableLiveData<Tab>()
    val perTabsCountLiveData = MutableLiveData<Map<Tab, Int>>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val currentSectionIdLiveData = SingleEventLiveData<String>()
    val repTypeDialogLiveData = SingleEventLiveData<RepairTypesFilterParams>()
    //endregion
    private val repairTypes = mutableSetOf<RepairTypeViewModel>()
    private var locomotives = emptyList<Locomotive>()
    private var activeTab = Tab.ALL
    private var sectionsDisposable = Disposables.disposed()
    private var selectSectionDisposable = Disposables.disposed()
    private var locomotivesFilterDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        loadData()
        activeTabLiveData.value = activeTab
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        sectionsDisposable.dispose()
        selectSectionDisposable.dispose()
        locomotivesFilterDisposable.dispose()
    }
    /**
     * Выбор секции
     */
    fun onSectionClicked(section: Section) {
        selectSectionDisposable = selectSectionInteractor.selectSection(section)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        navigator.navigateToMeasurementTypes()
                        currentSectionIdLiveData.value = sessionManager.currentSectionId
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * переключение вкладки
     */
    fun onTabClicked(tab: Tab) {
        if (tab != activeTab) {
            activeTab = tab
            filterLocomotives(sessionManager.equipmentFilter)

            activeTabLiveData.value = tab
        }
    }
    /**
     * вызов диалога выбора типа ремонта
     */
    fun onRepTypeFilterMenuItemClicked() {
        val (filterRepairTypes) = sessionManager.equipmentFilter
        val selectedRepairTypes = filterRepairTypes.map { RepairTypeViewModel(it.id, it.name) }
        repTypeDialogLiveData.value = Pair(repairTypes, selectedRepairTypes)
    }
    /**
     * выбор типа ремонта
     */
    fun onRepairTypesSelected(repairTypes: List<RepairTypeViewModel>) {
        val filterRepairTypes = repairTypes.map {
            RepairType().apply {
                id = it.id
                name = it.name
            }
        }
        val filter = sessionManager.equipmentFilter
        filterLocomotives(filter.copy(repairTypes = filterRepairTypes))
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        sectionsDisposable = sectionsLoader.loadLocoSections()
                .map { result ->
                    /**
                     * Обработка результата
                     */
                    locomotives = result.locomotives
                    selectRepairTypes(locomotives)
                    if (result.isSuccessful) {
                        val filter = sessionManager.equipmentFilter

                        Triple(result.userError, locomotives, resolvePerTabCount(locomotives, filter))
                    } else Triple(result.userError, null, null)
                }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ (userError, locomotives, perTabCount) ->
                    loadingLiveData.value = false
                    noDataLiveData.value = locomotives?.isEmpty() ?: true
                    if (userError == UserError.NO_ERROR) {
                        currentSectionIdLiveData.value = sessionManager.currentSectionId
                        locomotivesLiveData.value = locomotives
                        perTabsCountLiveData.value = perTabCount
                        /**
                         * Обработка ошибки
                         */
                    } else userErrorLiveData.value = userError
                }, { logger.error(it) })
    }
    /**
     * выбор типа ремонта
     */
    private fun selectRepairTypes(locomotives: List<Locomotive>) {
        for (locomotive in locomotives) {
            locomotive.sections.forEach { section ->
                section.repairType?.let {
                    repairTypes.add(RepairTypeViewModel(it.id, it.name))
                }
            }
        }
    }
    /**
     * Фильтрация списка локомотивов
     */
    private fun filterLocomotives(filter: EquipmentFilter) {
        locomotivesFilterDisposable.dispose()
        locomotivesFilterDisposable = Single.fromCallable {
            val filteredLocomotives = mutableListOf<Locomotive>()
            for (locomotive in locomotives) {
                val filteredSections = filterSections(locomotive, filter)

                val sectionsByTab = if (activeTab == Tab.ALL) {
                    filteredSections
                } else filteredSections.filter { Tab.ofSectionState(it.state) == activeTab }
                val shouldAddLocomotive = sectionsByTab.isNotEmpty()
                if (shouldAddLocomotive)
                    filteredLocomotives.add(locomotive)
            }

            Pair(filteredLocomotives, resolvePerTabCount(locomotives, filter))
        }.subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe { (locomotives, perTabCount) ->
                    locomotivesLiveData.value = locomotives
                    perTabsCountLiveData.value = perTabCount
                }
        sessionManager.equipmentFilter = filter
    }

    private fun resolvePerTabCount(locomotives: List<Locomotive>, filter: EquipmentFilter): Map<Tab, Int> {
        val perTabCount = mutableMapOf(
                Tab.ALL to 0,
                Tab.WAITING to 0,
                Tab.IN_SERVICE to 0
        )

        var allCount = 0
        for (locomotive in locomotives)
            filterSections(locomotive, filter).forEach { item ->
                val tab = Tab.ofSectionState(item.state)
                perTabCount[tab] = (perTabCount[tab] ?: 0) + 1
                allCount++
            }
        perTabCount[Tab.ALL] = allCount
        return perTabCount
    }
    /**
     * Фильтрация секций
     */
    private fun filterSections(locomotive: Locomotive, filter: EquipmentFilter): List<Section> {
        val (repairTypes) = filter
        return locomotive.sections.filter { section ->
            repairTypes.isEmpty() || repairTypes.any { it.id == section.repairType?.id }
        }
    }
}