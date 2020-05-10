package ru.digipeople.locotech.master.ui.activity.measurement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_measurement.*
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.MeasurementStatus
import ru.digipeople.locotech.master.model.MeasurementsTask
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.measurement.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.measurement.adapter.MeasurementAdapter
import ru.digipeople.locotech.master.ui.activity.measurement.dialog.MeasurementTaskDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.adapter.decoration.GridDividerItemDecoration
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.android.AndroidUtils
import ru.digipeople.utils.model.UserError
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Активити замеров
 *
 * @author Sostavkin Grisha
 */
class MeasurementActivity : AppCompatActivity() {
    //region Di
    private val component by lazy {
        AppComponent.get().measurementComponent(ActivityModule(this))
    }
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var navigator: Navigator
    //endregion
    //region Views
    lateinit var workTitle: TextView
    lateinit var status: TextView
    lateinit var buttonStatusRequest: Button
    //endregion
    //region Other
    private val adapter = MeasurementAdapter()
    private val dateTimeFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
    private lateinit var viewModel: MeasurementViewModel
    //endregion
    /**
     * действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement)

        status = findViewById(R.id.measure_list_item_status)
        buttonStatusRequest = findViewById(R.id.measure_list_item_status_btn)
        buttonStatusRequest.setOnClickListener { viewModel.onStatusBtnClicked() }
        workTitle = findViewById(R.id.measure_list_item_work_name)
        /**
         * инициализаци тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.measurement_title)
        /**
         * инициализация меню
         */
        mainDrawerDelegate.init(iconBack = true)

        recycler_view.apply {
            recycler_view.addItemDecoration(GridDividerItemDecoration(AndroidUtils.dpToPx(1f, context)) {
                it == MeasurementAdapter.VIEW_TYPE_MEASUREMENT || it == MeasurementAdapter.VIEW_TYPE_MEASUREMENT_TITLE
            })
            adapter = this@MeasurementActivity.adapter
        }
        /**
         * инициализаци viewModel
         */
        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(MeasurementViewModel::class.java)
        viewModel.apply {
            measurementParams = intent.getParcelableExtra(ARG_PARAMS)
            measurementsLiveData.observe({ lifecycle }, ::setData)
            loadingLiveData.observe({ lifecycle }, ::setLoadingVisible)
            userErrorLiveData.observe({ lifecycle }, ::showUserError)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            measurementTaskDialogLiveData.observe({ lifecycle }) { showMeasurementsTaskDialog() }
            measurementTaskLiveData.observe({ lifecycle }) { task -> showTaskInfoDialog(task) }
            statusLiveData.observe({ lifecycle }, ::setStatus)
            workNameLiveData.observe({ lifecycle }, ::setWorkName)
            start()
        }

        adapter.itemClickListener = viewModel::onMeasurementItemClicked
        adapter.commentClickListener = ::onCommentClicked
    }
    /**
     * создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.measurement_menu, menu)
        return true
    }
    /**
     * действия при возобновлении работы активити
     */
    override fun onResume() {
        super.onResume()
        viewModel.onRefreshClicked()
        navigator.onResume(this)
    }
    /**
     * действия при переходе активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * добавление пункта меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.measurement_menu_item_refresh -> {
                viewModel.onRefreshClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    /**
     * установка статуса задания на получение апп замера и функционала кнопки
     */
    private fun setStatus(status: MeasurementStatus) {
        when (status) {
            MeasurementStatus.NO_TASK -> {
                measure_list_item_status.text = getString(R.string.measurement_no_task)
                measure_list_item_status_btn.text = getString(R.string.measurement_status_btn_assign)
            }
            MeasurementStatus.WAITING -> {
                measure_list_item_status.text = getString(R.string.measurement_waiting)
                measure_list_item_status_btn.text = getString(R.string.measurement_status_btn_check_receive)
            }
            MeasurementStatus.RECEIVED -> {
                measure_list_item_status.text = getString(R.string.measurement_received)
                measure_list_item_status_btn.text = getString(R.string.measurement_status_btn_assign)
            }
        }
    }
    /**
     * обработка нажатия на комментарий
     */
    private fun onCommentClicked(measurement: Measurement) {
        val comment = measurement.measurementComment
        AlertDialog.Builder(this)
                .setTitle(R.string.measurement_detail_comment_title)
                .setMessage(getString(R.string.measurement_detail_comment_content,
                        comment.author, dateTimeFormatter.format(comment.dateTime), comment.text))
                .show()
    }
    /**
     * отображение диалога
     */
    private fun showMeasurementsTaskDialog() {
        MeasurementTaskDialog.show(supportFragmentManager) { stage, sectionNumber ->
            viewModel.onAssignmentParamsApplied(stage, sectionNumber)
        }
    }
    /**
     * установка имени работы
     */
    private fun setWorkName(workName: String) {
        workTitle.text = workName
    }
    /**
     * отображение диалога с информацией по задаче
     */
    private fun showTaskInfoDialog(task: MeasurementsTask) {
        AlertDialog.Builder(this)
                .setTitle(R.string.measurement_task_title)
                .setOnDismissListener { viewModel.onTaskReceived(task) }
                .setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        task.measurementsDevices)) { _, _ -> }
                .show()
    }
    /**
     * отображение сообщения об отсутствии данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        no_data.visibility = if (visible) View.VISIBLE else View.GONE
        measure_list_item_status.visibility = if (!visible) View.VISIBLE else View.GONE
        measure_list_item_status_btn.visibility = if (!visible) View.VISIBLE else View.GONE
    }
    /**
     * установка данных
     */
    private fun setData(data: AdapterData) = adapter.setData(data)
    /**
     * управление видимостью лоадера
     */
    private fun setLoadingVisible(visible: Boolean) = loadingFragmentDelegate.setLoadingVisibility(visible)
    /**
     * отображение ошибки пользователя
     */
    private fun showUserError(userError: UserError) {
        Snackbar.make(recycler_view, userError.message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_PARAMS = "ARG_PARAMS"
        /**
         * стандартный интент
         */
        fun getCallingIntent(context: Context, workId: String, workName: String, workStatus: Int): Intent {
            return Intent(context, MeasurementActivity::class.java)
                    .apply { putExtra(ARG_PARAMS, MeasurementParams(workId, workName, workStatus)) }
        }
    }
}