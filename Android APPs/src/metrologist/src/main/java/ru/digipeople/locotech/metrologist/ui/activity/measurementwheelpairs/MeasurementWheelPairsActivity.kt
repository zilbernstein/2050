package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs

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
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.adapter.DividerItemDecoration
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.adapter.WheelPairsAdapter
import ru.digipeople.locotech.metrologist.ui.view.MeasurementInfoView
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Декоратор ввода данных замера
 */
class MeasurementWheelPairsActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerMeasurementWheelPairsComponent.builder()
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
    lateinit var wheelPairsAdapter: WheelPairsAdapter
    //endregion
    //region Views
    private lateinit var noData: FrameLayout
    private lateinit var measurementInfoView: MeasurementInfoView
    private lateinit var wheelPairsRecyclerView: RecyclerView
    //endregion
    //region Other
    private lateinit var viewModel: MeasurementWheelPairsViewModel
    //endregion
    /**
     * Действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_measurement_wheel_pairs)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.measurement_wheel_pairs_title)
        toolbarDelegate.setNavigationIcon(ru.digipeople.locotech.core.R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        measurementInfoView = findViewById(R.id.measurement_info)
        noData = findViewById(R.id.noDataView)
        wheelPairsRecyclerView = findViewById(R.id.wheel_pairs_recycler)
        wheelPairsRecyclerView.layoutManager = LinearLayoutManager(this)
        wheelPairsRecyclerView.adapter = wheelPairsAdapter
        wheelPairsRecyclerView.addItemDecoration(DividerItemDecoration(this))

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(MeasurementWheelPairsViewModel::class.java)
        viewModel.apply {
            measurementLiveData.observe({ lifecycle }, ::setMeasurement)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            wheelPairsLiveData.observe({ lifecycle }, ::setWheelPairs)
            start()
        }

        wheelPairsAdapter.onToggleTurningListener = viewModel::onToggleWheelPairTurning
        wheelPairsAdapter.onEditBtnClickListener = viewModel::onEditWheelPairBtnClicked
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.measurement_wheel_pairs, menu)
        return true
    }
    /**
     * Создание меню
     */
    private fun setMeasurement(measurement: Measurement) {
        measurementInfoView.setMeasurement(measurement)
    }
    /**
     * Сообщение об отсутствии данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * Отображение ошибки
     */
    private fun showError(error: UserError) {
        Snackbar.make(wheelPairsRecyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Установка колесных пар
     */
    private fun setWheelPairs(wheelPairs: List<WheelPair>) {
        wheelPairsAdapter.items = wheelPairs
    }
    /**
     * установка пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_wheel_pairs -> {
                viewModel.onSaveBtnClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, MeasurementWheelPairsActivity::class.java)
        }
    }
}