package ru.digipeople.locotech.master.ui.activity.performance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.master.ui.activity.performance.adapter.DiffUtilCallback
import ru.digipeople.locotech.master.ui.activity.performance.adapter.PerformanceAdapter
import ru.digipeople.locotech.master.ui.activity.performance.model.Tab
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.locotech.master.ui.dialog.selectequipment.SelectEquipmentDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.delegate.WorkaroundFragmentDelegate
import ru.digipeople.ui.view.HorizontalSortView
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити исполнения
 *
 * @author Kashonkov Nikita
 */
class PerformanceActivity : MvpActivity(), PerformanceView {
    //region Di
    private lateinit var screenComponent: PerformanceScreenComponent
    private lateinit var component: PerformanceComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var workaroundFragmentDelegate: WorkaroundFragmentDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var parentLayout: androidx.drawerlayout.widget.DrawerLayout
    private lateinit var sortInApprove: HorizontalSortView
    private lateinit var sortNew: HorizontalSortView
    private lateinit var sortInTask: HorizontalSortView
    private lateinit var sortPerformancing: HorizontalSortView
    private lateinit var sortFinished: HorizontalSortView
    private lateinit var sortAccepted: HorizontalSortView
    private lateinit var sortAll: HorizontalSortView
    private lateinit var recyler: RecyclerView
    private lateinit var actionButton: Button
    private lateinit var menu: Menu
    //endregion
    //region Other
    private lateinit var presenter: PerformancePresenter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var activeTab: Tab
    private val performanceAdapter = PerformanceAdapter()
    private val scrollPositionMap = mutableMapOf<Tab, Int>()
    private val scrollOffsetMap = mutableMapOf<Tab, Int>()
    private lateinit var autoRefreshDelegate: PerformanceAutoRefreshDelegate
    private var selectEquipmentDialog: SelectEquipmentDialog? = null
    private var restoreScroll = false
    //endregion
    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.perfomanceComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfomance)
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()

        parentLayout = findViewById(R.id.drawerLayout)
        /**
         * инициализация вкладок
         */
        sortInApprove = findViewById(R.id.activity_performance_in_approve)
        sortInApprove.setOnClickListener { onTabLicked(Tab.IN_APPROVE) }

        sortNew = findViewById(R.id.activity_performance_new)
        sortNew.setOnClickListener { onTabLicked(Tab.NEW) }

        sortInTask = findViewById(R.id.activity_performance_in_task)
        sortInTask.setOnClickListener { onTabLicked(Tab.IN_TASK) }

        sortPerformancing = findViewById(R.id.activity_performance_performancing)
        sortPerformancing.setOnClickListener { onTabLicked(Tab.IN_WORK) }

        sortFinished = findViewById(R.id.activity_performance_finished)
        sortFinished.setOnClickListener { onTabLicked(Tab.FINISHED) }

        sortAccepted = findViewById(R.id.activity_performance_accepted)
        sortAccepted.setOnClickListener { onTabLicked(Tab.ACCEPTED) }

        sortAll = findViewById(R.id.activity_performance_all)
        sortAll.setOnClickListener { onTabLicked(Tab.ALL) }

        actionButton = findViewById(R.id.activity_performance_button)
        actionButton.visibility = View.GONE
        actionButton.setOnClickListener {
            if (sortFinished.isChosen) {
                presenter.onAcceptAllButtonClicked()
            } else {
                presenter.startAllWorkButtonClicked()
            }
        }
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.performancePresenter() }, PerformancePresenter::class.java)
        presenter.initialize()
        /**
         * инициализация ресайклера и адаптера
         */
        recyler = findViewById(R.id.activity_perfomance_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recyler.layoutManager = layoutManager

        performanceAdapter.menuItemClickListener = this::onAdapterMenuItemClicked
        performanceAdapter.stoppedClickListener = presenter::onStoppedWorkClicked
        performanceAdapter.returnClickListener = presenter::onReturnWorkClicked
        performanceAdapter.applyClickListener = presenter::onAcceptWorkClicked
        performanceAdapter.startClickListener = presenter::onStartWorkClicked
        recyler.adapter = performanceAdapter

        autoRefreshDelegate = PerformanceAutoRefreshDelegate(Handler(), performanceAdapter)
        /**
         * инициализация бокового меню
         */
       mainDrawerDelegate.init(false)
    }
    /**
     * выбор вкладки
     */
    private fun onTabLicked(tab: Tab) {
        restoreScroll = true
        val position = layoutManager.findFirstVisibleItemPosition()
        val view = layoutManager.findViewByPosition(position)
        scrollPositionMap[activeTab] = position
        scrollOffsetMap[activeTab] = view?.top ?: 0
        presenter.onTabClicked(tab)
    }
    /**
     * действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        autoRefreshDelegate.onStart()
        presenter.onScreenShown()
    }
    /**
     * действия при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        performanceAdapter.updateTime()
        navigator.onResume(this)
    }
    /**
     * действия при паузе
     */
    override fun onPause() {
        navigator.onPause()
        loadingFragmentDelegate.setLoadingVisibility(false)
        super.onPause()
    }
    /**
     * действия при остановке активити
     */
    override fun onStop() {
        autoRefreshDelegate.onStop()
        super.onStop()
    }
    /**
     * создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.performance_menu, menu)
        this.menu = menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val icon = DrawableCompat.wrap(item.icon).mutate()
            item.icon = icon
            updateMenuItemIconColor(item)
        }
        return true
    }
    /**
     * добавление пунктов меню (фильтров)
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            /**
             * ТМЦ
             */
            R.id.tmc -> {
                presenter.onToggleFilterWithTmc()
            }
            /**
             * Замеры
             */
            R.id.measurements -> {
                presenter.onToggleFilterWithMeasurements()
            }
            /**
             * МПИ
             */
            R.id.mpi -> {
                presenter.onToggleFilterWithMpi()
            }
            /**
             * смена секции
             */
            R.id.switch_equipment -> {
                onToggleSwitchEquipment()
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }
    /**
     * переключение секции
     */
    private fun onToggleSwitchEquipment() {
        if (selectEquipmentDialog == null) {
            selectEquipmentDialog = SelectEquipmentDialog.newInstance().apply {
                onDismissListener = {
                    selectEquipmentDialog = null
                }
                show(supportFragmentManager, null)
            }
        }
    }
    /**
     * установка наименвания секции
     */
    override fun setEquipmentName(equipmentName: String) {
        toolbarDelegate.setTitle(getString(R.string.performance_title, equipmentName))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_COMMENT_BEFORE_RETURN) {
            val isSuccesfull = CommentActivity.getResultFromIntent(resultCode, data)
            if (isSuccesfull) {
                presenter.onCommentAddedToReturnedWork()
            } else {
                presenter.onCommentAddedToReturnWorkMistake()
            }
        }
    }
    /**
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
//        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * установка выбранной вкладки
     */
    override fun setActiveTab(tab: Tab) {
        activeTab = tab
        sortInApprove.setSelect(false)
        sortNew.setSelect(false)
        sortInTask.setSelect(false)
        sortPerformancing.setSelect(false)
        sortFinished.setSelect(false)
        sortAccepted.setSelect(false)
        sortAll.setSelect(false)

        when (tab) {
            Tab.IN_APPROVE -> sortInApprove.setSelect(true)
            Tab.NEW -> sortNew.setSelect(true)
            Tab.IN_TASK -> sortInTask.setSelect(true)
            Tab.IN_WORK -> sortPerformancing.setSelect(true)
            Tab.FINISHED -> sortFinished.setSelect(true)
            Tab.ACCEPTED -> sortAccepted.setSelect(true)
            Tab.ALL -> sortAll.setSelect(true)
        }
    }
    /**
     * установка кол-ва работ во вкладке
     */
    override fun setPerTabCount(perTabCount: Map<Tab, Int>) {
        for ((key, value) in perTabCount) {
            when (key) {
                Tab.IN_APPROVE -> sortInApprove.setSortedCount(value)
                Tab.NEW -> sortNew.setSortedCount(value)
                Tab.IN_TASK -> sortInTask.setSortedCount(value)
                Tab.IN_WORK -> sortPerformancing.setSortedCount(value)
                Tab.FINISHED -> sortFinished.setSortedCount(value)
                Tab.ACCEPTED -> sortAccepted.setSortedCount(value)
                Tab.ALL -> sortAll.setSortedCount(value)
            }
        }
    }
    /**
     * загрузка данных в адаптер
     */
    override fun setDataToAdapter(works: List<Work>) {
        if (restoreScroll) {
            performanceAdapter.items = works
            performanceAdapter.notifyDataSetChanged()
            val position = scrollPositionMap[activeTab] ?: 0
            val offset = scrollOffsetMap[activeTab] ?: 0
            layoutManager.scrollToPositionWithOffset(position, offset)
            restoreScroll = false
        } else {
            val diffUtilCallback = DiffUtilCallback(performanceAdapter.items, works)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
            performanceAdapter.items = works
            diffResult.dispatchUpdatesTo(performanceAdapter)
        }
    }
    /**
     * установка функционеала кнопке Запустить все
     */
    override fun setActionButtonForNewWork() {
        actionButton.visibility = View.VISIBLE
        actionButton.setText(R.string.performance_activity_start_all)
    }
    /**
     * установка функционала кнопке Принять все
     */
    override fun setActionButtonForDoneWork() {
        actionButton.visibility = View.VISIBLE
        actionButton.setText(R.string.performance_activity_accept_all)
    }
    /**
     * сделать кнопку невидимой
     */
    override fun hideActionButton() {
        actionButton.visibility = View.GONE
    }
    /**
     * ошибка назначения на работу
     */
    override fun showEditPerformersError() {
        Snackbar.make(parentLayout, R.string.performance_activity_performance_mistake, Snackbar.LENGTH_LONG).show()
    }
    /**
     * ошибка принятия пустого списка
     */
    override fun showAcceptAllError() {
        Snackbar.make(parentLayout, R.string.performance_activity_accept_all_mistake, Snackbar.LENGTH_LONG).show()
    }
    /**
     * отображение ошибки
     */
    override fun showError(userError: UserError) {
        Snackbar.make(parentLayout, userError.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * ошибка нет исполнителя
     */
    override fun showNoPerformanceError() {
        Snackbar.make(parentLayout, R.string.performance_activity_start_work_mistake, Snackbar.LENGTH_LONG).show()
    }
    /**
     * ошибка пустой список работ
     */
    override fun showEmptyStartListError() {
        Snackbar.make(parentLayout, R.string.performance_activity_start_all_work_mistake, Snackbar.LENGTH_LONG).show()
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * установка фильтра ТМЦ
     */
    override fun setTmcFilterChecked(checked: Boolean) {
        val menuItem = menu.findItem(R.id.tmc)
        menuItem.isChecked = checked
        updateMenuItemIconColor(menuItem)
    }
    /**
     * установка фильтра Замеры
     */
    override fun setMeasurementsFilterChecked(checked: Boolean) {
        val menuItem = menu.findItem(R.id.measurements)
        menuItem.isChecked = checked
        updateMenuItemIconColor(menuItem)
    }
    /**
     * Установка фильтра МПИ
     */
    override fun setMpiFilterChecked(checked: Boolean) {
        val menuItem = menu.findItem(R.id.mpi)
        menuItem.isChecked = checked
        updateMenuItemIconColor(menuItem)
    }
    /**
     * обработка действи в контекстном меню
     */
    private fun onAdapterMenuItemClicked(menuItemType: MenuItemType, work: Work) {
        val action = when (menuItemType) {
            MenuItemType.PERFORMER -> presenter::onPerformanceClicked
            MenuItemType.PARTIALLY_ACCEPTED -> presenter::onPartlyAcceptClicked
            MenuItemType.COMMENT -> presenter::onEditCommentClicked
            MenuItemType.TMC -> presenter::onTmcClicked
            MenuItemType.PHOTO -> presenter::onPhotoClicked
            MenuItemType.MEASUREMENT -> presenter::onMeasurementClicked
            MenuItemType.DIVIDE -> presenter::onDivideClicked
            MenuItemType.WORKAROUND -> this::onWorkaroundClicked
            else -> return
        }
        action.invoke(work)
    }
    /**
     * обработа обходного решения
     */
    private fun onWorkaroundClicked(work: Work) {
        workaroundFragmentDelegate.show({ presenter.onWorkaroundClicked(work) })
    }
    /**
     * изменение цвета иконки
     */
    private fun updateMenuItemIconColor(item: MenuItem) {
        val color = if (item.isChecked) {
            R.color.ic_performance_filter_checked
        } else {
            R.color.ic_performance_filter_default
        }
        val tintColor = ContextCompat.getColor(this, color)
        DrawableCompat.setTint(item.icon, tintColor)
    }

    private fun getScreenComponent(): PerformanceScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().perfomanceScreenComponent()
        } else {
            return saved as PerformanceScreenComponent
        }
    }

    companion object {
        val EDIT_COMMENT_BEFORE_RETURN: Int = 100
        /**
         * интент
         */
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, PerformanceActivity::class.java)
        }
    }
}