package ru.digipeople.locotech.metrologist.ui.activity.profilometers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.R.drawable
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.Profilometer
import ru.digipeople.locotech.metrologist.ui.activity.profilometers.adapter.AdapterData
import ru.digipeople.locotech.metrologist.ui.activity.profilometers.adapter.DividerItemDecorator
import ru.digipeople.locotech.metrologist.ui.activity.profilometers.adapter.ProfilometerMeasurementsAdapter
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Экран "Профилометры".
 */
class ProfilometersActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerProfilometersComponent.builder()
                .profilometersModule(ProfilometersModule(this))
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Other
    //region Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var noData: FrameLayout
    //endregion
    private lateinit var viewModel: ProfilometersViewModel
    private val adapter = ProfilometerMeasurementsAdapter()
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilometers)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.profilometers_title)
        toolbarDelegate.setNavigationIcon(drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecorator(this))
        recyclerView.adapter = adapter

        noData = findViewById(R.id.noDataView)

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(ProfilometersViewModel::class.java)
                .also {
                    it.donorId = intent.getStringExtra(EXTRA_MEASUREMENT_ID)
                    it.profilometersLiveData.observe({ lifecycle }, ::setProfilometers)
                    it.loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
                    it.noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
                    it.userErrorLiveData.observe({ lifecycle }, ::showError)
                    it.start()
                }
        adapter.onMeasureClickListener = viewModel::onMeasurementClicked
    }
    /**
     * Управлени видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Отображение сообщения об отсутствие данных
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
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка профилометра
     */
    private fun setProfilometers(profilometerMeasurements: Map<String, List<Measurement>>) {
        val profilometersAndMeasurements: MutableList<Any> = mutableListOf()
        profilometerMeasurements.entries.forEach {
            val profilometer = Profilometer()
            profilometer.number = it.key
            profilometer.measurementList.addAll(it.value)
            profilometersAndMeasurements.add(profilometer)
        }
        val adapterData = AdapterData()
        adapterData.addAll(profilometersAndMeasurements)
        adapter.items = adapterData
        adapter.notifyDataSetChanged()
    }

    companion object {

        private const val EXTRA_MEASUREMENT_ID = "EXTRA_MEASUREMENT_ID"

        fun getCallingIntent(context: Context, measurementId: String): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, ProfilometersActivity::class.java)
            intent.putExtra(EXTRA_MEASUREMENT_ID, measurementId)
            return intent
        }
    }
}