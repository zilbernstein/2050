package ru.digipeople.locotech.metrologist.ui.activity.editmeasurement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import ru.digipeople.locotech.metrologist.data.model.WheelParam
import ru.digipeople.locotech.metrologist.ui.activity.editmeasurement.adapter.WheelParamsAdapter
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.adapter.HeaderDecoration
import ru.digipeople.locotech.metrologist.ui.view.MeasurementInfoView
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * активити редактирования данных по кп
 *
 * @author Michael Solenov
 */
class EditMeasurementActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerEditMeasurementComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var wheelParamsAdapter: WheelParamsAdapter
    //endregion
    //region Views
    private lateinit var measurementInfoView: MeasurementInfoView
    private lateinit var kpTitle: TextView
    private lateinit var axisNumberValue: TextView
    private lateinit var axisCountValue: TextView
    private lateinit var flangeLeftNumberValue: TextView
    private lateinit var flangeRightNumberValue: TextView
    private lateinit var paramsRecyclerView: RecyclerView
    //endregion
    //region Other
    private lateinit var viewModel: EditMeasurementViewModel
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_edit_measurement)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.edit_measurement_title)
        toolbarDelegate.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        measurementInfoView = findViewById(R.id.measurement_info)
        kpTitle = findViewById(R.id.kp_title)
        axisNumberValue = findViewById(R.id.axis_number_value)
        axisCountValue = findViewById(R.id.axis_count_value)
        flangeLeftNumberValue = findViewById(R.id.flange_left_number_value)
        flangeRightNumberValue = findViewById(R.id.flange_right_number_value)

        paramsRecyclerView = findViewById(R.id.parametr_recycler)
        paramsRecyclerView.layoutManager = LinearLayoutManager(this)
        paramsRecyclerView.setHasFixedSize(true)
        paramsRecyclerView.addItemDecoration(HeaderDecoration(paramsRecyclerView, resources.getDimensionPixelOffset(R.dimen.offset_item_wheel_param_header)))
        paramsRecyclerView.adapter = wheelParamsAdapter

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(EditMeasurementViewModel::class.java)
        viewModel.apply {
            wheelPairPosition = intent.getIntExtra(EXTRA_WHEEL_PAIR_POSITION, 0)
            measurementLiveData.observe({ lifecycle }, ::setMeasurement)
            wheelParamsLiveData.observe({ lifecycle }, ::setWheelParams)
            wheelPairLiveData.observe({ lifecycle }, ::setWheelPair)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            start()
        }
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_measurement, menu)
        return true
    }
    /**
     * Установка пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.check -> {
                viewModel.onSaveBtnClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    /**
     * Установка параметров колесных пар
     */
    private fun setWheelParams(wheelParams: List<WheelParam<*>>) {
        wheelParamsAdapter.items = wheelParams
    }
    /**
     * Установка замеров
     */
    private fun setMeasurement(measurement: Measurement) {
        measurementInfoView.setMeasurement(measurement)
    }
    /**
     * Установка колесных пар
     */
    private fun setWheelPair(wheelPair: WheelPair) {
        kpTitle.text = getString(R.string.measurement_wheel_pair_number, wheelPair.number)
        axisNumberValue.text = wheelPair.axisNumber
        axisCountValue.text = wheelPair.number
        flangeLeftNumberValue.text = wheelPair.flangeLeftNumber
        flangeRightNumberValue.text = wheelPair.flangeRightNumber
    }
    /**
     * Отображение ошибки
     */
    private fun showError(error: UserError) {
        Snackbar.make(measurementInfoView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управление видимосьбю лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }

    companion object {
        private const val EXTRA_WHEEL_PAIR_POSITION = "EXTRA_WHEEL_PAIR_POSITION"

        fun getCallingIntent(context: Context, wheelPairPosition: Int): Intent {
            val intent = Intent(context, EditMeasurementActivity::class.java)
            intent.putExtra(EXTRA_WHEEL_PAIR_POSITION, wheelPairPosition)
            return intent
        }
    }
}