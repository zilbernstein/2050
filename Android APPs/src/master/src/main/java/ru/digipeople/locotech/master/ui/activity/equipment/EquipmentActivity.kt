package ru.digipeople.locotech.master.ui.activity.equipment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.EquipmentAdapter
import ru.digipeople.locotech.master.ui.activity.equipment.model.Tab
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.dialog.repairtype.RepairTypeFilterDialog
import ru.digipeople.ui.dialog.repairtype.RepairTypeFilterDialogListener
import ru.digipeople.ui.dialog.repairtype.RepairTypeViewModel
import ru.digipeople.ui.view.HorizontalSortView
import javax.inject.Inject

/**
 * Активити локомотивов на учатске
 * @author Kashonkov Nikita
 */
class EquipmentActivity : MvpActivity(), EquipmentView, RepairTypeFilterDialogListener {
    //region DI
    private lateinit var screenComponent: EquipmentScreenComponent
    private lateinit var component: EquipmentComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var equipmentAdapter: EquipmentAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //region other
    //region View
    private lateinit var tabAll: HorizontalSortView
    private lateinit var tabWaiting: HorizontalSortView
    private lateinit var tabInService: HorizontalSortView
    //endregion
    //region Other
    private lateinit var presenter: EquipmentPresenter
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private var locomotiveListAutorefreshDelegate: EquipmentAutorefreshDelegate? = null
    private var repairTypesDialog: RepairTypeFilterDialog? = null
    //endRegion
    /**
     * операции при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.locomotiveComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locomotive)
        /**
         * инициализация тулбара и установка заголовка
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.locomotive_activity_title)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.locomotivePresenter() }, EquipmentPresenter::class.java)
        presenter.initialize()
        /**
         * инициализация меню
         */
        mainDrawerDelegate.init(false)
        /**
         * определение вкладок и установка обработчиков нажатия
         * вкладка все
         */
        tabAll = findViewById(R.id.tab_eq_status_all)
        tabAll.setOnClickListener { presenter.onTabClicked(Tab.ALL) }
        /**
         * вкладка "на ожидании"
         */
        tabWaiting = findViewById(R.id.tab_eq_status_waiting)
        tabWaiting.setOnClickListener { presenter.onTabClicked(Tab.WAITING) }
        /**
         * вкладка "в работе"
         */
        tabInService = findViewById(R.id.tab_eq_status_in_service)
        tabInService.setOnClickListener { presenter.onTabClicked(Tab.IN_SERVICE) }
        /**
         * инициализация ресайклер вью и адаптера
         */
        recyclerView = findViewById(R.id.activity_locomotive_recycler_view)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = equipmentAdapter

        equipmentAdapter.sectionClickListner = presenter::onSectionClicked
        equipmentAdapter.lineEquipmentClickListener = presenter::onLineEquipmentClicked
    }
    /**
     * операции, выполняемые при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
        locomotiveListAutorefreshDelegate = EquipmentAutorefreshDelegate(Handler(), equipmentAdapter)
        locomotiveListAutorefreshDelegate!!.onStart()
    }
    /**
     * создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.equipment_menu, menu)
        return true
    }
    /**
     * реализация поведения при нажатии на пункт меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.equipment_rep_type_menu_item) {
            presenter.onRepTypeFilterMenuItemClicked()
            true
        } else false
    }
    /**
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
    }
    /**
     * установка выбранной вкладки
     */
    override fun setActiveTab(tab: Tab) {
        tabAll.setSelect(tab == Tab.ALL)
        tabWaiting.setSelect(tab == Tab.WAITING)
        tabInService.setSelect(tab == Tab.IN_SERVICE)
    }
    /**
     * подсчет секций во вкладке
     */
    override fun setPerTabCount(perTabCount: Map<Tab, Int>) {
        for ((key, value) in perTabCount) {
            when (key) {
                Tab.ALL -> tabAll.setSortedCount(value)
                Tab.WAITING -> tabWaiting.setSortedCount(value)
                Tab.IN_SERVICE -> tabInService.setSortedCount(value)
            }
        }
    }
    /**
     * выбор типа ремонта
     */
    override fun onRepairTypesSelected(repairTypes: List<RepairTypeViewModel>) {
        presenter.onRepairTypesSelected(repairTypes)
    }
    /**
     * операции при возобновлении работы активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * операции при переходе активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * операции при остановке активити
     */
    override fun onStop() {
        super.onStop()
        locomotiveListAutorefreshDelegate!!.onStop()
    }
    /**
     * операции при уничтожении активити
     */
    override fun onDestroy() {
        presenter.onDestroy()
        locomotiveListAutorefreshDelegate?.onStop()
        repairTypesDialog = null
        super.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * установка данных в адаптер
     */
    override fun setData(adapterData: AdapterData) {
        equipmentAdapter.setData(adapterData)
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        loadingFragmentDelegate.setLoadingVisibility(false)
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * отображение ошибки (без параметров)
     */
    override fun showError() { }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * диалог выбора типа ремонта для фильтрации
     */
    override fun showRepairTypesDialog(repairTypes: Set<RepairTypeViewModel>, selected: List<RepairTypeViewModel>) {
        RepairTypeFilterDialog.show(repairTypes, selected, this)
    }

    private fun getScreenComponent(): EquipmentScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().locomotiveScreenComponent()
        } else {
            return saved as EquipmentScreenComponent
        }
    }

    companion object {
        /**
         * стандартный интент
         */
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, EquipmentActivity::class.java)
        }

    }
}