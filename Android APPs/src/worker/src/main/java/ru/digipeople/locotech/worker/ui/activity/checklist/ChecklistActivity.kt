package ru.digipeople.locotech.worker.ui.activity.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.ChecklistItem
import ru.digipeople.locotech.worker.ui.activity.checklist.adapter.ChecklistAdapter
import ru.digipeople.locotech.worker.ui.activity.checklist.decoration.DividerItemDecoration
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити чеклист
 *
 * @author Sostavkin Grisha
 */
class ChecklistActivity : AppActivity() {
    //region DI
    private val component by lazy {
        DaggerChecklistComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    //endregion
    //region Views
    lateinit var recycler: RecyclerView
    private lateinit var noData: FrameLayout
    //endregion
    //region Other
    private lateinit var viewModel: ChecklistViewModel
    private val adapter = ChecklistAdapter()
    //endregion
    /**
     * Действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.checklist_title)
        toolbarDelegate.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }
        /**
         * Инициализация ресайклера
         */
        recycler = findViewById(R.id.activity_checklist_recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this))
        recycler.adapter = adapter
        noData = findViewById(R.id.noDataView)

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(ChecklistViewModel::class.java)
        viewModel.apply {
            /**
             * Получение данных из интента
             */
            workId = intent.getStringExtra(EXTRA_WORK_ID)
            loadingLiveData.observe({ lifecycle }, ::setLoadingVisible)
            userErrorLiveData.observe({ lifecycle }, ::showUserError)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            checklistLiveData.observe({ lifecycle }, ::setData)

            start()
        }

        adapter.onToggleListener = viewModel::onToggleChecklistItem
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.checklist_menu, menu)
        return true
    }
    /**
     * Добавление пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tech -> {
                viewModel.onTechClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    /**
     * Отображение сообщения что нет данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * Установка данных
     */
    private fun setData(checklist: List<ChecklistItem>) {
        adapter.items = checklist
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setLoadingVisible(visible: Boolean) = loadingFragmentDelegate.setLoadingVisibility(visible)
    /**
     * Отображение ошибки
     */
    private fun showUserError(userError: UserError) {
        Snackbar.make(recycler, userError.message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_WORK_ID = "EXTRA_WORK_ID"
        /**
         * Интент
         */
        fun getCallingIntent(context: Context, workId: String) = Intent(context, ChecklistActivity::class.java).apply {
            putExtra(EXTRA_WORK_ID, workId)
        }
    }
}