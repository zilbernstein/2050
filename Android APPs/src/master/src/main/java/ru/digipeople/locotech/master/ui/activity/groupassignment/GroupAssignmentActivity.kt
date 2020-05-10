package ru.digipeople.locotech.master.ui.activity.groupassignment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig.BrigAssignmentAdapter
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig.BrigadeAdapterData
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group.GroupAdapterData
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group.GroupAssignmentAdapter
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити группового назначения исполнителей
 *
 * @author Sostavkin Grisha
 */
class GroupAssignmentActivity : MvpActivity(), GroupAssignmentView {
    //region DI
    private lateinit var screenComponent: GroupAssignmentScreenComponent
    private lateinit var component: GroupAssignmentComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var groupAdapter: GroupAssignmentAdapter
    @Inject
    lateinit var brigAdapter: BrigAssignmentAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region View
    private lateinit var groupRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var brigRecycleView: androidx.recyclerview.widget.RecyclerView
    private lateinit var presenter: GroupAssignmentPresenter
    //endregion
    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.groupAssignmentComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_assignment)
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.group_assignment_title)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, GroupAssignmentPresenter::class.java)
        presenter.initialize()
        /**
         * инициализация меню
         */
        mainDrawerDelegate.init(false)
        /**
         * инициализация ресайклеров групп и бригад
         */
        groupRecyclerView = findViewById(R.id.activity_group_assignment_recycler_view)
        val groupLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        groupRecyclerView.layoutManager = groupLayoutManager

        brigRecycleView = findViewById(R.id.activity_brig_assignment_recycler_view)
        val brigLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        brigRecycleView.layoutManager = brigLayoutManager
        /**
         * установка декораторов для ресайклеров
         */
        val groupDividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, groupLayoutManager.orientation)
        groupDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        groupRecyclerView.addItemDecoration(groupDividerItemDecoration)

        val brigDividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, brigLayoutManager.orientation)
        brigDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        brigRecycleView.addItemDecoration(brigDividerItemDecoration)
        /**
         * установка адаптера
         */
        groupRecyclerView.adapter = groupAdapter
        brigRecycleView.adapter = brigAdapter

        groupAdapter.itemGroupCheckClickListener = presenter::onGroupClicked
        groupAdapter.itemCheckClickListener = presenter::onWorkClicked

        brigAdapter.itemWorkerOnClickListener = presenter::onWorkerClicked
        brigAdapter.itemBrigTitleOnClickListener = presenter::onBrigClicked

        findViewById<Button>(R.id.choose_assignment_templates_btn)
                .setOnClickListener { navigator.navigateToAssignmentTemplates() }
    }
    /**
     * действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * действия при  возобновлении работы активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * действия при паузе
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
    }
    /**
     * добавление меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_with_check, menu)
        return true
    }
    /**
     * определение новых пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.check -> {
                presenter.onAssignmentClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    /**
     * загрузка данных в адаптер групп
     */
    override fun setDataToGroupAdapter(groupAdapterData: GroupAdapterData) {
        groupAdapter.setData(groupAdapterData)
    }
    /**
     * загрузка данных в адаптер бригад
     */
    override fun setDataToBrigAdapter(brigadeAdapterData: BrigadeAdapterData) {
        brigAdapter.setData(brigadeAdapterData)
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        loadingFragmentDelegate.setLoadingVisibility(false)
        Snackbar.make(groupRecyclerView, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * отображение ошибки назначения сотрудника из разных бригад
     */
    override fun showBrigadeError() {
        Snackbar.make(groupRecyclerView, getString(R.string.group_assignment_brig_error), Snackbar.LENGTH_LONG).show()
    }
    /**
     * отображение ошибки макс числа исполнителей
     */
    override fun showMaxWorkersError() {
        Snackbar.make(groupRecyclerView, getString(R.string.group_assignment_max_workers_error), Snackbar.LENGTH_LONG).show()
    }
    /**
     * отображение ошибки недоступности исполнителя
     */
    override fun showWorkerError() {
        Snackbar.make(groupRecyclerView, getString(R.string.group_assignment_worker_error), Snackbar.LENGTH_LONG).show()
    }
    /**
     * отображение ошибки не выбранной работы
     */
    override fun showWorkEmptyError() {
        Snackbar.make(groupRecyclerView, getString(R.string.group_assignment_work_empty_error), Snackbar.LENGTH_LONG).show()
    }
    /**
     * Отображение ошибки не выбранного рабочего
     */
    override fun showWorkerEmptyError() {
        Snackbar.make(groupRecyclerView, getString(R.string.group_assignment_worker_empty_error), Snackbar.LENGTH_LONG).show()
    }
    /**
     * Отображение ошибки недоступности рабочих
     */
    override fun showWorkersError() {
        Snackbar.make(groupRecyclerView, getString(R.string.group_assignment_workers_error), Snackbar.LENGTH_LONG).show()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    private fun getScreenComponent(): GroupAssignmentScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        return if (saved == null) {
            AppComponent.get().groupAssignmentScreenComponent()
        } else {
            saved as GroupAssignmentScreenComponent
        }
    }

    companion object {
        /**
         * стандартный интент
         */
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, GroupAssignmentActivity::class.java)
        }
    }
}
