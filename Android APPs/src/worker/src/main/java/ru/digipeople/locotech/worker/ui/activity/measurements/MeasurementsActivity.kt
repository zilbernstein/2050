package ru.digipeople.locotech.worker.ui.activity.measurements

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_measurements.*
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.measurements.adapter.MeasurementsAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити замеры
 */
class MeasurementsActivity : AppCompatActivity() {
    //region DI
    private val component: MeasurementsComponent by lazy {
        AppComponent.get().measurementsComponent(ActivityModule(this))
    }
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragment: LoadingFragmentDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    //endregion
    //region Other
    private val adapter: MeasurementsAdapter = MeasurementsAdapter()
    private lateinit var viewModel: MeasurementsViewModel
    private lateinit var params: MeasurementsParams
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurements)
        setupToolbar()
        /**
         * Получение данных из интента
         */
        val extras = intent.extras!!
        params = extras.getParcelable(PARAMS)!!

        measure_list_title.text = params.workName

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(MeasurementsViewModel::class.java)
        viewModel.params = params
        viewModel.apply {
            stagesLiveData.observe({ lifecycle }, ::setVisibleStages)
            measurementsLiveData.observe({ lifecycle }, ::setData)
            loadingLiveData.observe({ lifecycle }, ::setLoading)
            userErrorLiveData.observe({ lifecycle }, ::showUserError)
            start()
        }
        /**
         * Переключение по вкладкам
         */
        tabs.setOnCheckedChangeListener { _, checkedId ->
            val stage = when (checkedId) {
                /**
                 * До ремонта
                 */
                R.id.beforeRepairTab -> Stage.BEFORE_REPAIR
                /**
                 * Контрольный
                 */
                R.id.controlTab -> Stage.CONTROL
                /**
                 * После ремонта
                 */
                R.id.afterRepairTab -> Stage.AFTER_REPAIR
                else -> throw IllegalArgumentException("unknown tab id")
            }
            viewModel.onStageSelected(stage)
        }

        measurements_recycler.adapter = adapter.apply {
            onCommentClickListener = viewModel::onCommentClicked
        }
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.measurements, menu)
        return true
    }
    /**
     * Добавление пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_menu_item -> viewModel.onRefreshBtnClicked()
            R.id.save_menu_item -> {
                val editableMeasurements = adapter.editableMeasurements.values.toList()
                viewModel.onSaveBtnClicked(editableMeasurements)
            }
            else -> return false
        }
        return true
    }
    /**
     * Действия при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при приостановке активити
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * Отображение ошибки
     */
    private fun showUserError(error: UserError) {
        Snackbar.make(measurements_recycler, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка данных
     */
    private fun setData(measurements: List<Measurement>) {
        setNoDataMsgVisible(measurements.isEmpty())
        adapter.items = measurements
        //Flush all edited items
        adapter.editableMeasurements.clear()
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setLoading(isLoading: Boolean) {
        loadingFragment.setLoadingVisibility(isLoading)
    }
    /**
     * Отображение сообшения нет данных
     */
    private fun setNoDataMsgVisible(visible: Boolean) {
        if (visible) {
            empty_text.visibility = View.VISIBLE
        } else {
            empty_text.visibility = View.GONE
        }
    }
    /**
     * Отображение этапов
     */
    private fun setVisibleStages(stages: List<Stage>) {
        val setVisibility = { view: View, stage: Stage ->
            view.visibility = if (stages.contains(stage)) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        setVisibility(beforeRepairTab, Stage.BEFORE_REPAIR)
        setVisibility(controlTab, Stage.CONTROL)
        setVisibility(afterRepairTab, Stage.AFTER_REPAIR)

        // Checking if current tab is hidden
        val selectedTab = tabs.findViewById<RadioButton>(tabs.checkedRadioButtonId)
        if (selectedTab.visibility == View.VISIBLE) {
            return
        }
        // Setting new selected tab if current is hidden
        for (i in 0 until tabs.childCount) {
            val child = tabs.getChildAt(i) as RadioButton
            if (child.visibility == View.VISIBLE) {
                child.isChecked = true
                break
            }
        }
    }
    /**
     * Установка тулбара
     */
    private fun setupToolbar() = with(toolbarDelegate) {
        init()
        setTitle(R.string.measure_title)
        setNavigationIcon(ru.digipeople.locotech.core.R.drawable.ic_toolbar_back)
        setNavigationOnClickListener { onBackPressed() }
    }

    companion object {
        private const val PARAMS = "PARAMS_EXTRA"
        const val START_COMMENT_REQUEST = 103

        fun getCallingIntent(context: Context, params: MeasurementsParams): Intent {
            return Intent(context, MeasurementsActivity::class.java)
                    .apply { putExtra(PARAMS, params) }
        }
    }
}