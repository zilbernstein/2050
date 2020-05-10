package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Reason
import ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.adapter.DividerItemDecorator
import ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.adapter.TuningReasonsAdapter
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити выбора причины обточки
 *
 * @author Michael Solenov
 */
class TuningReasonsActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerTuningReasonsComponent.builder()
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var noData: FrameLayout
    //endregion
    //region Other
    private lateinit var viewModel: TuningReasonsViewModel
    private var adapter = TuningReasonsAdapter()
    //endregion
    /**
     * Действяи при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_turning_reasons)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.tuning_reasons_title)
        toolbarDelegate.setNavigationIcon(ru.digipeople.locotech.core.R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        recyclerView = findViewById(R.id.facing_reasons_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecorator(this))
        recyclerView.adapter = adapter
        noData = findViewById(R.id.noDataView)

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(TuningReasonsViewModel::class.java)
        viewModel.apply {
            wheelPairPosition = intent.getIntExtra(EXTRA_WHEEL_PAIR_POSITION, 0)
            reasonsLiveData.observe({ lifecycle }, ::setReasons)
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            start()
        }

        adapter.onItemClickListener = viewModel::onReasonClicked
    }
    /**
     * Отображение ошибки
     */
    private fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Сообщение об отсутствии даннных
     */
    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * загрузка причин
     */
    private fun setReasons(reasons: List<Reason>) {
        adapter.items = reasons
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }

    companion object {
        private const val EXTRA_WHEEL_PAIR_POSITION = "EXTRA_WHEEL_PAIR_POSITION"

        fun getCallingIntent(context: Context, wheelPairPosition: Int): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, TuningReasonsActivity::class.java)
            intent.putExtra(EXTRA_WHEEL_PAIR_POSITION, wheelPairPosition)
            return intent
        }
    }
}