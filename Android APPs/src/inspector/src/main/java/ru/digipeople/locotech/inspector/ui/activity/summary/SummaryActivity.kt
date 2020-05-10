package ru.digipeople.locotech.inspector.ui.activity.summary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.summary.adapter.SummaryAdapter
import ru.digipeople.locotech.inspector.ui.activity.summary.adapter.SummaryItem
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Экран "Сводная информация"
 *
 * @author Kashonkov Nikita
 */
class SummaryActivity : MvpActivity(), SummaryView {
    //region Di
    private lateinit var screenComponent: SummaryScreenComponent
    private lateinit var component: SummaryComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var adapter: SummaryAdapter
    //end region
    //region View
    private lateinit var printBtn: FloatingActionButton
    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    //end region
    //region Other
    lateinit var presenter: SummaryPresenter
    //end region
    /**
     * действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.equipment_activity_title)
        /**
         * инициализаци презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, SummaryPresenter::class.java)
        presenter.initialize()
        /**
         * инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        /*clickedCyclicInterceptor.setOnClickListener {
            viewModel.onCyclicWorkClicked()
        }

        clickedOtkRemarkInterceptor.setOnClickListener {
            viewModel.onRemarkOtkClicked()
        }

        clickedRzdRemarkInterceptor.setOnClickListener { viewModel.onRzdRemarkClicked() }

        clickedCheckListInterceptor.setOnClickListener { viewModel.onCheckListClicked() }*/

        printBtn = findViewById(R.id.print_btn)
        printBtn.setOnClickListener { presenter.onPrintButtonClicked() }

        recycler = findViewById(R.id.summary_recycler)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.layoutManager = linearLayoutManager

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, linearLayoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        recycler.addItemDecoration(dividerItemDecoration)

        recycler.adapter = adapter

    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
        presenter.onScreenShown()
    }

    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установка заголовка
     */
    override fun setTitle(equipmentName: String) {
        toolbarDelegate.setTitle(getString(R.string.summary_activity_title, equipmentName))
    }
    /**
     * установка данных
     */
    override fun setData(summaryItemList: List<SummaryItem>) {
        adapter.items = summaryItemList
    }
    /**
     * управление видимостью лоадера
     */
    override fun showLoading(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * отображние ошибки
     */
    override fun showError(error: UserError) {
        Snackbar.make(printBtn, error.message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): SummaryScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        return if (saved == null) {
            AppComponent.get().summaryScreenComponent()
        } else {
            saved as SummaryScreenComponent
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SummaryActivity::class.java)
        }

    }
}