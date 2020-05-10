package ru.digipeople.locotech.metrologist.ui.activity.measurementtypes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.MeasurementCategory
import ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.adapter.MeasurementTypesAdapter
import ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.decorator.DividerItemDecorator
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Активити типов замеров
 */
class MeasurementTypesActivity : AppActivity() {
    // region Di
    private val component by lazy {
        DaggerMeasurementTypesComponent.builder()
                .measurementTypesModule(MeasurementTypesModule(this))
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
    //region Views
    private lateinit var sectionName: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noData: FrameLayout
    //endregion
    //region Other
    private lateinit var viewModel: MeasurementTypesViewModel
    private val adapter = MeasurementTypesAdapter()
    //endregion
    /**
     * Действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_measurement_types)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.measurement_types_title)
        mainDrawerDelegate.init(false)

        sectionName = findViewById(R.id.sectionName)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecorator(this))
        recyclerView.adapter = adapter
        noData = findViewById(R.id.noDataView)

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(MeasurementTypesViewModel::class.java)
        viewModel.also {
            it.measurementsCategoriesLiveData.observe({ lifecycle }, ::setCategories)
            it.noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            it.loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            it.userErrorLiveData.observe({ lifecycle }, ::showError)
            it.sectionNameLiveData.observe({ lifecycle }, ::setSectionName)
            it.start()
        }
        /**
         * обработка нажатия на тип замера
         */
        adapter.onItemClickListener = viewModel::onCategoryClicked
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Отображениие сообщенияоб отсутствии данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * установка наименования секции
     */
    private fun setSectionName(sectionName: String) {
        this.sectionName.text = getString(R.string.measurement_types_section_name, sectionName)
    }
    /**
     * Отображение ошибки
     */
    private fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка категорий
     */
    private fun setCategories(categories: List<MeasurementCategory>) {
        adapter.items = categories
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, MeasurementTypesActivity::class.java)
        }
    }
}
