package ru.digipeople.locotech.inspector.ui.activity.measurement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.measurement.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.measurement.adapter.MeasurementsAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити замеров
 *
 * @author Michael Solenov
 */
class MeasurementActivity : MvpActivity(), MeasurementView {
    //region DI
    private lateinit var screenComponent: MeasurementScreenComponent
    private lateinit var component: MeasurementComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var measurementsAdapter: MeasurementsAdapter
    //end Region
    //region View
    private lateinit var recycle: androidx.recyclerview.widget.RecyclerView
    private lateinit var workTitleTextView: TextView
    private lateinit var backBtn: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var refreshBtn: AppCompatImageView
    private lateinit var noDataView: FrameLayout
    private lateinit var noDataTextView: TextView
    //endRegion
    //region Other
    private lateinit var presenter: MeasurementPresenter
    private lateinit var workId: String
    private lateinit var workName: String
    private var workStatus: Int = 0
    //end Region
    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent.extras!!
        /**
         * получение данных из интента
         */
        workId = intent.get(EXTRA_WORK_ID) as String
        workName = intent.get(EXTRA_WORK_NAME) as String
        workStatus = (intent.get(EXTRA_WORK_STATUS) as Int)
        screenComponent = getScreenComponent()
        component = screenComponent.measurementsComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, MeasurementPresenter::class.java)
        presenter.initialize()

        workTitleTextView = findViewById(R.id.work_name)
        workTitleTextView.text = workName

        titleTextView = findViewById(R.id.title)
        titleTextView.text = getString(R.string.measurement_title)

        backBtn = findViewById(R.id.back_button)
        backBtn.setOnClickListener { presenter.onBackBtnClicked() }

        noDataView = findViewById(R.id.noDataView)
        noDataTextView = findViewById(R.id.noDataMessage)
        noDataTextView.text = resources.getString(R.string.measurements_no_data)

        refreshBtn = findViewById(R.id.refresh)

        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycle = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.measurement_recycle).apply {
            adapter = measurementsAdapter
            layoutManager = linearLayoutManager
        }
        /**
         * обработка кнопки обновить
         */
        refreshBtn.setOnClickListener {
            presenter.loadData(workId)
        }

        measurementsAdapter.commentClickListener = presenter::onCommentCLicked

    }
    /**
     * действия при старте активити
     */
    override fun onStart() {
        presenter.onScreenShow(workId)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }

    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoading(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recycle, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * установка данных
     */
    override fun setData(items: List<Any>) {
        noDataView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        val adapterData = AdapterData()
                .apply { addAll(items) }
        measurementsAdapter.setData(adapterData)
    }

    private fun getScreenComponent(): MeasurementScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        return if (saved == null) {
            AppComponent.get().measurementComponent()
        } else {
            saved as MeasurementScreenComponent
        }
    }

    companion object {

        private const val EXTRA_WORK_ID = "EXTRA_WORK_ID"
        private const val EXTRA_WORK_NAME = "EXTRA_WORK_NAME"
        private const val EXTRA_WORK_STATUS = "EXTRA_WORK_STATUS"

        fun getCallingIntent(context: Context, workId: String, workName: String, workStatus: Int): Intent {
            /**
             * интент
             */
            val intent = Intent(context, MeasurementActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            intent.putExtra(EXTRA_WORK_NAME, workName)
            intent.putExtra(EXTRA_WORK_STATUS, workStatus)
            return intent
        }
    }
}