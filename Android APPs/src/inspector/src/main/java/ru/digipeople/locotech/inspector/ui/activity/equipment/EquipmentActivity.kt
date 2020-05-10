package ru.digipeople.locotech.inspector.ui.activity.equipment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.EquipmentAdapter
import ru.digipeople.locotech.inspector.ui.activity.equipment.model.Tab
import ru.digipeople.locotech.inspector.ui.dialog.RepairTypeFilterDialog
import ru.digipeople.locotech.inspector.ui.dialog.RepairTypeFilterDialogListener
import ru.digipeople.locotech.inspector.ui.dialog.RepairTypeViewModel
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.HorizontalSortView
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити локомотивов
 */
class EquipmentActivity : AppActivity(), RepairTypeFilterDialogListener {
    //region Di
    private val component by lazy {
        DaggerEquipmentComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .coreAppComponent(CoreAppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var tabAll: HorizontalSortView
    private lateinit var tabWaiting: HorizontalSortView
    private lateinit var tabInService: HorizontalSortView
    //endregion
    //region Other
    private val viewModel by lazy {
        ViewModelProviders.of(this, component.viewModelFactory()).get(EquipmentViewModel::class.java)
    }
    private val equipmentAdapter = EquipmentAdapter()
    private lateinit var autoRefreshDelegate: EquipmentAutoRefreshDelegate
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locomotive)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.equipment_activity_title)
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        recyclerView = findViewById(R.id.activity_locomotive_recycler_view)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = equipmentAdapter

        tabAll = findViewById(R.id.tab_eq_status_all)
        tabAll.setOnClickListener { viewModel.onTabClicked(Tab.ALL) }
        tabWaiting = findViewById(R.id.tab_eq_status_waiting)
        tabWaiting.setOnClickListener { viewModel.onTabClicked(Tab.WAITING) }
        tabInService = findViewById(R.id.tab_eq_status_in_service)
        tabInService.setOnClickListener { viewModel.onTabClicked(Tab.IN_SERVICE) }

        equipmentAdapter.onSectionClickListener = viewModel::onSectionClicked
        equipmentAdapter.onLineEquipmentClickListener = viewModel::onLineEquipmentClicked

        viewModel.apply {
            userErrorLiveData.observe({ lifecycle }, ::showUserError)
            equipmentsLiveData.observe({ lifecycle }, ::setAdapter)
            loadingLiveData.observe({ lifecycle }, ::setLoadingVisible)
            perTabCountLiveData.observe({ lifecycle }, ::setPerTabCount)
            activeTabLiveData.observe({ lifecycle }, ::setActiveTab)
            repairTypesFilterDialogLiveData.observe({ lifecycle }) { (repairTypes, selected) ->
                showRepairTypesDialog(repairTypes, selected)
            }

            start()
        }
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.equipment_menu, menu)
        return true
    }
    /**
     * Обработка пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.equipment_rep_type_menu_item) {
            viewModel.onRepTypeFilterMenuItemClicked()
            true
        } else false
    }
    /**
     * Переход назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
    }
    /**
     * Лействия при старте активити
     */
    override fun onStart() {
        super.onStart()
        viewModel.onScreenShown()
        autoRefreshDelegate = EquipmentAutoRefreshDelegate(Handler(), equipmentAdapter)
        autoRefreshDelegate.onStart()
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }

    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        autoRefreshDelegate.onStop()
    }
    /**
     * Выбор типа ремонта
     */
    override fun onRepairTypesSelected(repairTypes: List<RepairTypeViewModel>) {
        viewModel.onRepairTypesSelected(repairTypes)
    }
    /**
     * Установка данных в адаптер
     */
    private fun setAdapter(equipments: AdapterData) {
        equipmentAdapter.items = equipments
    }
    /**
     * Отображнеи ошибки
     */
    private fun showUserError(userError: UserError) {
        Snackbar.make(recyclerView, userError.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управлнеи видимостью лоадера
     */
    private fun setLoadingVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Установка активной вкладки
     */
    private fun setActiveTab(tab: Tab) {
        tabAll.setSelect(tab == Tab.ALL)
        tabWaiting.setSelect(tab == Tab.WAITING)
        tabInService.setSelect(tab == Tab.IN_SERVICE)
    }
    /**
     * Установка количества работ по статусам
     */
    private fun setPerTabCount(perTabCount: Map<Tab, Int>) {
        for ((key, value) in perTabCount) {
            when (key) {
                Tab.ALL -> tabAll.setSortedCount(value)
                Tab.WAITING -> tabWaiting.setSortedCount(value)
                Tab.IN_SERVICE -> tabInService.setSortedCount(value)
            }
        }
    }
    /**
     * Диалог с типами ремонта
     */
    private fun showRepairTypesDialog(repairTypes: Set<RepairTypeViewModel>, selected: List<RepairTypeViewModel>) {
        RepairTypeFilterDialog.show(repairTypes, selected, this)
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, EquipmentActivity::class.java)
        }

    }

}