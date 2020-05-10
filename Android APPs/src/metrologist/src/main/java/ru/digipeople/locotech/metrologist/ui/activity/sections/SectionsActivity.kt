package ru.digipeople.locotech.metrologist.ui.activity.sections

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Locomotive
import ru.digipeople.locotech.metrologist.ui.activity.sections.adapter.AdapterData
import ru.digipeople.locotech.metrologist.ui.activity.sections.adapter.DividerItemDecorator
import ru.digipeople.locotech.metrologist.ui.activity.sections.adapter.SectionsAdapter
import ru.digipeople.locotech.metrologist.ui.activity.sections.model.Tab
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.dialog.repairtype.RepairTypeFilterDialog
import ru.digipeople.ui.dialog.repairtype.RepairTypeFilterDialogListener
import ru.digipeople.ui.dialog.repairtype.RepairTypeViewModel
import ru.digipeople.ui.view.HorizontalSortView
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити секций
 *
 * @author Nikita Sychev
 **/
class SectionsActivity : AppActivity(), RepairTypeFilterDialogListener {
    //region Di
    private val component by lazy {
        DaggerSectionsComponent.builder()
                .sectionsModule(SectionsModule(this))
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var noData: FrameLayout
    private lateinit var tabAll: HorizontalSortView
    private lateinit var tabWaiting: HorizontalSortView
    private lateinit var tabInService: HorizontalSortView
    //endregion
    //region Other
    private lateinit var viewModel: SectionsViewModel
    private val adapter = SectionsAdapter()
    //endregion
    /**
     * Действия при стрте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sections)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.sections_title)
        mainDrawerDelegate.init(false)

        tabAll = findViewById(R.id.tab_eq_status_all)
        tabAll.setOnClickListener { viewModel.onTabClicked(Tab.ALL) }
        tabWaiting = findViewById(R.id.tab_eq_status_waiting)
        tabWaiting.setOnClickListener { viewModel.onTabClicked(Tab.WAITING) }
        tabInService = findViewById(R.id.tab_eq_status_in_service)
        tabInService.setOnClickListener { viewModel.onTabClicked(Tab.IN_SERVICE) }

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                .apply {
                    addItemDecoration(DividerItemDecorator(this@SectionsActivity))
                    adapter = this@SectionsActivity.adapter
                }

        noData = findViewById(R.id.noDataView)
        /**
         * Работа с viewModel
         */
        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(SectionsViewModel::class.java)
                .apply {
                    loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
                    locomotivesLiveData.observe({ lifecycle }, ::setLocomotives)
                    noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
                    perTabsCountLiveData.observe({ lifecycle }, ::setPerTabsCount)
                    activeTabLiveData.observe({ lifecycle }, ::setActiveTab)
                    userErrorLiveData.observe({ lifecycle }, ::showError)
                    currentSectionIdLiveData.observe({ lifecycle }, ::setCurrentSectionId)
                    repTypeDialogLiveData.observe({ lifecycle }, ::showRepairTypesDialog)

                    start()
                }
        adapter.onSectionClickListener = viewModel::onSectionClicked
    }
    /**
     * Созздание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sections_menu, menu)
        return true
    }
    /**
     * управление пунктами меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.sections_rep_type_filter_menu_item) {
            viewModel.onRepTypeFilterMenuItemClicked()
            true
        } else false
    }
    /**
     * переход назад
     */
    override fun onBackPressed() {
        // nop
    }
    /**
     * Выбор тпа ремонта
     */
    override fun onRepairTypesSelected(repairTypes: List<RepairTypeViewModel>) {
        viewModel.onRepairTypesSelected(repairTypes)
    }
    /**
     * диалог выбора типа ремонта
     */
    private fun showRepairTypesDialog(repairTypesParams: Pair<Set<RepairTypeViewModel>, List<RepairTypeViewModel>>) {
        val (repairTypes, selected) = repairTypesParams
        RepairTypeFilterDialog.show(repairTypes, selected, this)
    }
    /**
     * Установка выбранной вкладки
     */
    private fun setActiveTab(tab: Tab) {
        tabAll.setSelect(tab == Tab.ALL)
        tabWaiting.setSelect(tab == Tab.WAITING)
        tabInService.setSelect(tab == Tab.IN_SERVICE)
    }
    /**
     * Установка числа по вкладкам
     */
    private fun setPerTabsCount(perTabsCount: Map<Tab, Int>) {
        for ((tab, count) in perTabsCount)
            when (tab) {
                Tab.ALL -> tabAll.setSortedCount(count)
                Tab.WAITING -> tabWaiting.setSortedCount(count)
                Tab.IN_SERVICE -> tabInService.setSortedCount(count)
            }
    }
    /**
     * Установить id текущей секции
     */
    private fun setCurrentSectionId(currentSectionId: String) {
        adapter.currentSectionId = currentSectionId
        for (position in adapter.items.indices) {
            if (adapter.items.isLocomotive(position)) {
                val locomotive = adapter.items.getLocomotive(position)
                val sectionPosition = locomotive.sections.indexOfFirst { it.id == currentSectionId }
                if (sectionPosition != -1) {
                    if (!adapter.expandedLocomotives.contains(locomotive.id)) {
                        adapter.toggleExpanded(locomotive, position)
                    }
                    adapter.notifyDataSetChanged()
                    return
                }
                recyclerView.scrollToPosition(position + sectionPosition + 1)
            }
        }
    }
    /**
     * соббщение об осутствии данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * Оторажение ошибки
     */
    private fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка локомотива
     */
    private fun setLocomotives(locomotives: List<Locomotive>) {
        adapter.expandedLocomotives.clear()
        adapter.items = AdapterData(locomotives)
        adapter.notifyDataSetChanged()
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SectionsActivity::class.java)
        }
    }
}