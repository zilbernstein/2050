package ru.digipeople.locotech.metrologist.ui.activity.measurementdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.core.ui.dialog.AppAlertDialog
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.core.ui.valueeditor.DateTimeValueEditor
import ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.adapter.MeasurementParamsAdapter
import ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.model.ScreenState
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
/**
 * Активити детальной информации замера
 */
class MeasurementDetailActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerMeasurementDetailComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var infoGroup: ConstraintLayout
    private lateinit var viewModel: MeasurementDetailViewModel
    private lateinit var infoTitle: TextView
    private lateinit var sectionValue: TextView
    private lateinit var equipmentValue: TextView
    private lateinit var typeValue: TextView
    private lateinit var kindValue: TextView
    private lateinit var dateTimeValue: TextView
    private lateinit var toolMeasurement: TextView
    private lateinit var profilometerValue: TextView
    private lateinit var profilometerLabel: TextView
    private lateinit var workerLabel: TextView
    private lateinit var workerValue: TextView
    private lateinit var manualMeasurement: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnDetail: Button
    private lateinit var noData: FrameLayout
    private lateinit var data: ConstraintLayout
    //endregion
    private lateinit var dateTimeValueEditor: DateTimeValueEditor
    private val adapter = MeasurementParamsAdapter()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd / HH:mm", Locale.getDefault())
    /**
     * Действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_measurement_detail)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.measurement_detail_title)
        toolbarDelegate.setNavigationIcon(ru.digipeople.locotech.core.R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        data = findViewById(R.id.data)
        infoGroup = findViewById(R.id.info_group)
        infoTitle = findViewById(R.id.info_title)
        sectionValue = findViewById(R.id.section_value)
        equipmentValue = findViewById(R.id.equipment_value)
        typeValue = findViewById(R.id.type_value)
        kindValue = findViewById(R.id.kind_value)

        dateTimeValue = findViewById(R.id.date_time_value)
        /**
         * Обработка установки времени
         */
        dateTimeValueEditor = DateTimeValueEditor(dateTimeValue).apply {
            onValueChangedListener = {
                it?.let {
                    viewModel.onDateTimeSelected(it)
                }
            }
            format = { it?.let { dateFormat.format(it) } ?: "" }
        }

        toolMeasurement = findViewById(R.id.tool_measurement)
        toolMeasurement.setOnClickListener { viewModel.onToolMeasurementClicked() }
        manualMeasurement = findViewById(R.id.manual_measurement)
        manualMeasurement.setOnClickListener { viewModel.onManualMeasurementClicked() }
        profilometerLabel = findViewById(R.id.profilometer_label)
        profilometerValue = findViewById(R.id.profilometer_value)
        profilometerValue.setOnClickListener { viewModel.onProfilometerClicked() }

        workerLabel = findViewById(R.id.worker_label)
        workerValue = findViewById(R.id.worker_value)
        btnDetail = findViewById(R.id.btn_detail)
        btnDetail.setOnClickListener { viewModel.onDetailBtnClicked() }

        recyclerView = findViewById(R.id.params_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        noData = findViewById(R.id.noDataView)
        /**
         * Работа с viewModel
         */
        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(MeasurementDetailViewModel::class.java)
        viewModel.apply {
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID)
            screenStateLiveData.observe({ lifecycle }, ::setScreenState)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            changeProfilometerWarningLiveData.observe({ lifecycle }, {
                showChangeProfilometerWarningDialog()
            })
            setManualModeWarningLiveData.observe({ lifecycle }, {
                showSetManualModeWarningDialog()
            })
            start()
        }
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.measurement_detail, menu)
        return true
    }
    /**
     * Установка пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                viewModel.onSaveBtnClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    /**
     * Сообщение об отсутствии данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) View.VISIBLE else View.GONE
        data.visibility = if (visible) View.GONE else View.VISIBLE
    }
    /**
     * управленеи видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Установка данных
     */
    private fun setScreenState(screenState: ScreenState) {
        val measurement = screenState.measurement

        dateTimeValueEditor.value = measurement.date

        sectionValue.text = measurement.sectionName
        equipmentValue.text = measurement.equipmentName
        typeValue.text = measurement.type
        kindValue.text = measurement.category
        profilometerValue.text = measurement.profilometerNumber
        workerValue.text = measurement.creator.name

        val isManual = measurement.isManual
        manualMeasurement.isSelected = isManual
        workerLabel.isEnabled = isManual
        workerValue.isEnabled = isManual
        toolMeasurement.isSelected = !isManual
        profilometerLabel.isEnabled = !isManual
        profilometerValue.isEnabled = !isManual

        adapter.items = screenState.parameters

        workerValue.setTextColor(ContextCompat.getColorStateList(this, R.color.text_default))
        profilometerValue.setTextColor(ContextCompat.getColorStateList(this, R.color.text_default))
    }

    private fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }

    private fun showChangeProfilometerWarningDialog() {
        AppAlertDialog(this)
                .apply {
                    title = getString(R.string.measurement_detail_attention)
                    message = getString(R.string.measurement_detail_change_profilometer_warning)
                    onOkBtnClickListener = viewModel::onChangeProfilometerApproved
                }
                .show()
    }

    private fun showSetManualModeWarningDialog() {
        AppAlertDialog(this)
                .apply {
                    title = getString(R.string.measurement_detail_attention)
                    message = getString(R.string.measurement_detail_set_manual_mode_warning)
                    onOkBtnClickListener = viewModel::onSetManualModeApproved
                }.show()
    }

    companion object {
        private const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"

        fun getCallingIntent(context: Context, categoryId: String): Intent {
            val intent = Intent(context, MeasurementDetailActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY_ID, categoryId)
            return intent
        }
    }
}