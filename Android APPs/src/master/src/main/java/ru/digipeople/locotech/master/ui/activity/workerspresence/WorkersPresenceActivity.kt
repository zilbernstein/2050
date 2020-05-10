package ru.digipeople.locotech.master.ui.activity.workerspresence

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_workers_presence.*
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.WorkersPresenceAdapter
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.WorkerPresenceItem
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.adapter.decoration.GridDividerItemDecoration
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.android.AndroidUtils
import ru.digipeople.utils.input.Keyboard
import ru.digipeople.utils.model.UserError
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
/**
 * Активити явка сотрудников
 */
class WorkersPresenceActivity : AppCompatActivity() {
    //region DI
    private val component: WorkersPresenceComponent by lazy {
        AppComponent.get().workersPresenceComponent(ActivityModule(this))
    }
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Other
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val adapter = WorkersPresenceAdapter()
    private lateinit var viewModel: WorkersPresenceViewModel
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workers_presence)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(getString(R.string.workers_presence_title, dateFormat.format(Date())))
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        recycler_view.apply {
            addItemDecoration(GridDividerItemDecoration(AndroidUtils.dpToPx(1f, context)) { it == WorkersPresenceAdapter.VIEW_TYPE_WORKER_PRESENCE })
            adapter = this@WorkersPresenceActivity.adapter
        }

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(WorkersPresenceViewModel::class.java)
        viewModel.apply {
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            brigadesLiveData.observe({ lifecycle }, ::setData)
            editableItemLiveData.observe({ lifecycle }, ::setEditableItem)
            start()
        }
        /**
         * Обработка нажатий на изменение и сохранение
         */
        adapter.onEditBtnClickListener = viewModel::onEditBtnClicked
        adapter.onSaveBtnClickListener = viewModel::onSaveBtnClicked
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.worker_presence_menu, menu)
        resolveOptionsMenu()
        return true
    }
    /**
     * Добавление пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.presence_menu_item_cancel -> {
                viewModel.onCancelClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    private fun resolveOptionsMenu() {
        val hasEditableItem = viewModel.editableItemLiveData.value != null
        toolbarDelegate.setOptionsMenuVisible(hasEditableItem)
    }
    /**
     * Отображение ошибки
     */
    private fun showError(userError: UserError) {
        Snackbar.make(recycler_view, userError.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка данных
     */
    private fun setData(items: List<Any>) {
        adapter.items = AdapterData(items)
        adapter.notifyDataSetChanged()
    }
    /**
     * Установка сообщения об отсутствии данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        no_data.visibility = if (visible) View.VISIBLE else View.GONE
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Управление возможнгостью редактировать элемент
     */
    private fun setEditableItem(item: WorkerPresenceItem?) {
        if (item == null) Keyboard.hide(recycler_view)
        resolveOptionsMenu()
        adapter.setEditableItem(item)
    }

    companion object {
        fun getCallingIntent(context: Context) = Intent(context, WorkersPresenceActivity::class.java)
    }
}