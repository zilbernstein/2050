package ru.digipeople.locotech.master.ui.activity.measurementedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.data.model.MeasureValueType
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.measurement.MeasureParams
import ru.digipeople.locotech.master.ui.activity.measurementedit.dialog.MeasurementEditWarnDialog
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Активити редактирования замера
 */
class MeasurementEditActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerMeasurementEditComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .coreAppComponent(CoreAppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var workNameTextView: TextView
    private lateinit var measureItemTitle: TextView
    private lateinit var stage: TextView
    private lateinit var accuracyTextView: TextView
    private lateinit var valueTextView: EditText
    private lateinit var checkRadioGroup: RadioGroup
    private lateinit var checkYes: RadioButton
    private lateinit var checkNo: RadioButton
    private lateinit var norm: TextView
    private lateinit var performer: TextView
    private lateinit var date: TextView
    private lateinit var comment: TextView
    private lateinit var eyeImage: ImageView
    //endregion
    //region Other
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private lateinit var viewModel: MeasurementEditViewModel
    private lateinit var measureParams: MeasureParams
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement_edit)
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.measurement_edit_title)
        toolbarDelegate.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        measureParams = intent.getParcelableExtra(EXTRA_MEASUREMENT)
        initViews()

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(MeasurementEditViewModel::class.java)
        viewModel.apply {

            loadingLiveData.observe({ lifecycle }, ::setLoadingVisible)
            userErrorLiveData.observe({ lifecycle }, ::showUserError)

            start()
        }
    }
    /**
     * создание модуля меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.measurement_edit, menu)
        return true
    }
    /**
     * добавление пункта меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.apply -> {
                if (!measureParams.isHardwareMeasurement) {
                    prepareParameters()
                } else {
                    showWarnDialog()
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    /**
     * действия при возобновлении работы активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * действия при постановки активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    private fun initViews() {
        workNameTextView = findViewById(R.id.work_name)
        measureItemTitle = findViewById(R.id.measure_list_item_name)
        stage = findViewById(R.id.measure_list_item_title)
        accuracyTextView = findViewById(R.id.measure_list_item_value_title)
        valueTextView = findViewById(R.id.measure_list_item_value)
        checkYes = findViewById(R.id.measure_list_item_value_check_yes)
        checkNo = findViewById(R.id.measure_list_item_value_check_no)
        norm = findViewById(R.id.measure_list_item_normal)
        performer = findViewById(R.id.measure_list_item_worker)
        date = findViewById(R.id.measure_list_item_date)
        comment = findViewById(R.id.measure_list_item_no_comment)
        checkRadioGroup = findViewById(R.id.measure_radio_group)
        eyeImage = findViewById(R.id.measure_comment_eye)

        bindViews()
    }
    /**
     * заполнение шаблона данными
     */
    private fun bindViews() {
        workNameTextView.text = measureParams.workName
        if (measureParams.characteristicName.isNotEmpty() && measureParams.measureName.isNotEmpty()) {
            measureItemTitle.text = getString(R.string.measurement_name_value, measureParams.measureName, measureParams.characteristicName)
        } else {
            measureItemTitle.text = getString(R.string.measurement_name_value_empty, measureParams.measureName)
        }
        norm.text = measureParams.measureNorm
        performer.text = measureParams.performerName
        date.text = measureParams.measureDate?.let { dateFormat.format(it) } ?: ""
        comment.text = measureParams.commentText

        accuracyTextView.text = getString(R.string.measurement_edit_item_value_title, "")

        if (measureParams.commentText.isNotEmpty()) {
            eyeImage.visibility = View.VISIBLE
            comment.visibility = View.GONE
            eyeImage.setOnClickListener {
                navigator.navigateToReadOnlyComment(measureParams.commentText)
            }
        } else {
            eyeImage.visibility = View.GONE
            comment.visibility = View.VISIBLE
            comment.text = getString(R.string.measurement_value_false)
        }
        /**
         * разделение по типам (до ремонта - после ремонта - контрольный)
         */
        when (measureParams.measureStage) {
            Stage.AFTER_REPAIR -> {
                stage.text = Stage.AFTER_REPAIR.title
            }
            Stage.BEFORE_REPAIR -> {
                stage.text = Stage.BEFORE_REPAIR.title
            }
            Stage.CONTROL -> {
                stage.text = Stage.CONTROL.title
            }
        }

        when (measureParams.measureType) {
            /**
             * если замер булевого типа
             */
            MeasureValueType.BOOLEAN -> {
                checkRadioGroup.visibility = View.VISIBLE
                if (measureParams.measureValue == "true") {
                    checkYes.isChecked = true
                } else {
                    checkNo.isChecked = true
                }
            }
            /**
             * если замер строковый
             */
            MeasureValueType.STRING -> {
                valueTextView.visibility = View.VISIBLE
                valueTextView.setText(measureParams.measureValue)
                valueTextView.setSelection(valueTextView.length())
                valueTextView.requestFocus()
            }
        }
    }
    /**
     * отображение диалога
     */
    private fun showWarnDialog() {
        MeasurementEditWarnDialog.show(supportFragmentManager) {
            prepareParameters()
        }
    }
    /**
     * управление видимостью лоадера
     */
    private fun setLoadingVisible(visible: Boolean) = loadingFragmentDelegate.setLoadingVisibility(visible)
    /**
     * отображеение ошибки
     */
    private fun showUserError(userError: UserError) {
        Snackbar.make(valueTextView, userError.message, Snackbar.LENGTH_SHORT).show()
    }
    /**
     * подготовка параметров к требуемому виду
     */
    private fun prepareParameters() {
        val value = when (measureParams.measureType) {
            MeasureValueType.BOOLEAN -> {
                if (checkYes.isChecked) {
                    "true"
                } else {
                    "false"
                }
            }
            MeasureValueType.STRING -> {
                valueTextView.text.toString()
            }
        }
        viewModel.onApplyBtnClicked(measureParams, value)
    }

    companion object {
        private const val EXTRA_MEASUREMENT = "EXTRA_MEASUREMENT"
        /**
         * стандартный интент
         */
        fun getCallingIntent(context: Context, params: MeasureParams): Intent {
            val intent = Intent(context, MeasurementEditActivity::class.java)
            intent.putExtra(EXTRA_MEASUREMENT, params)
            return intent
        }
    }
}
