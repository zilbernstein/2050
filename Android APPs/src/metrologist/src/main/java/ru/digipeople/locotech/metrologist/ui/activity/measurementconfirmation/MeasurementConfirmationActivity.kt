package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.Remark
import ru.digipeople.locotech.metrologist.data.model.WheelPairShort
import ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.adapter.DividerItemDecoration
import ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.adapter.RemarksAdapter
import ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.adapter.WheelPairsAdapter
import ru.digipeople.locotech.metrologist.ui.view.MeasurementInfoView
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * активити замечаний
 */
class MeasurementConfirmationActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerMeasurementConfirmationComponent.builder()
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
    private lateinit var measurementInfoView: MeasurementInfoView
    private lateinit var remarksNoData: View
    private lateinit var wheelPairsRecyclerView: RecyclerView
    private lateinit var remarksRecyclerView: RecyclerView
    //endregion
    private lateinit var viewModel: MeasurementConfirmationViewModel
    private var wheelPairsAdapter = WheelPairsAdapter()
    private var remarksAdapter = RemarksAdapter()
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_measurement_confirmation)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.measurement_confirmation_title)
        toolbarDelegate.setNavigationIcon(ru.digipeople.locotech.core.R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        measurementInfoView = findViewById(R.id.measurement_info)
        remarksNoData = findViewById(R.id.remarksNoData)

        wheelPairsRecyclerView = findViewById(R.id.wheelPairsRecyclerView)
        wheelPairsRecyclerView.layoutManager = LinearLayoutManager(this)
        wheelPairsRecyclerView.adapter = wheelPairsAdapter
        wheelPairsRecyclerView.addItemDecoration(DividerItemDecoration(this))

        remarksRecyclerView = findViewById(R.id.remarksRecyclerView)
        remarksRecyclerView.layoutManager = LinearLayoutManager(this)
        remarksRecyclerView.adapter = remarksAdapter
        remarksRecyclerView.addItemDecoration(DividerItemDecoration(this))

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(MeasurementConfirmationViewModel::class.java)
        viewModel.apply {
            measurementLiveData.observe({ lifecycle }, ::setMeasurement)
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            wheelPairsLiveData.observe({ lifecycle }, ::setWheelPairs)
            remarksLiveData.observe({ lifecycle }, ::setRemarks)
            remarksNoDataLiveData.observe({ lifecycle }, ::setRemarksNoDataVisible)
            start()
        }
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.measurement_confirmation, menu)
        return true
    }
    /**
     * Установка пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.apply -> {
                viewModel.onApplyBtnClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    /**
     * Установка замера
     */
    private fun setMeasurement(measurement: Measurement) {
        measurementInfoView.setMeasurement(measurement)
    }
    /**
     * Установка колесной пары
     */
    private fun setWheelPairs(wheelPairs: List<WheelPairShort>) {
        wheelPairsAdapter.items = wheelPairs
    }
    /**
     * Установка замечания
     */
    private fun setRemarks(remarks: List<Remark>) {
        remarksAdapter.items = remarks
    }
    /**
     * Отображение сообщения нет данных в замечании
     */
    private fun setRemarksNoDataVisible(visible: Boolean) {
        remarksNoData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Отображение ошибки
     */
    private fun showError(error: UserError) {
        Snackbar.make(wheelPairsRecyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, MeasurementConfirmationActivity::class.java)
        }
    }
}