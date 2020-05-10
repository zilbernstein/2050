package ru.digipeople.locotech.metrologist.ui.activity.reports

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Report
import ru.digipeople.locotech.metrologist.ui.activity.reports.adapter.ReportsAdapter
import ru.digipeople.locotech.metrologist.ui.activity.reports.dialog.SendReportDialog
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.HttpUtils
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Экран "Отчеты".
 */
class ReportsActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerReportsComponent.builder()
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
    //region Views
    private lateinit var noData: FrameLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    //endregion
    //region Other
    private lateinit var viewModel: ReportsViewModel
    private val reportsAdapter = ReportsAdapter()
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_reports)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.reports_title)
        mainDrawerDelegate.init(false)

        noData = findViewById(R.id.noDataView)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        viewPager.isUserInputEnabled = false
        viewPager.adapter = reportsAdapter
        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = reportsAdapter.getItem(position)?.name
        }).attach()
        /**
         * Обработка выбора вкладки
         */
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) { // nop
            }

            override fun onTabUnselected(tab: TabLayout.Tab) { // nop
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                reportsAdapter.getItem(tab.position)?.let {
                    viewModel.onReportSelected(it)
                }
            }

        })

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(ReportsViewModel::class.java)
        viewModel.apply {
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            reportsLiveData.observe({ lifecycle }, ::setReports)
            authInfoLiveData.observe({ lifecycle }, ::setAuthInfo)
            start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.reports, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send_report -> {
                onSendReportClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        // nop
    }

    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }

    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showError(error: UserError) {
        Snackbar.make(viewPager, error.message, Snackbar.LENGTH_LONG).show()
    }

    private fun setReports(reports: List<Report>) {
        reportsAdapter.items = reports
    }

    private fun setAuthInfo(usernamePasswordPair: Pair<String, String>) {
        val header = HttpUtils.buildBasicAuthHeader(
                usernamePasswordPair.first,
                usernamePasswordPair.second
        )
        reportsAdapter.webViewHeaders["Authorization"] = header
    }

    private fun onSendReportClicked() {
        SendReportDialog(this)
                .apply {
                    onSendBtnClicked = viewModel::onSendBtnClicked
                }
                .show()
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ReportsActivity::class.java)
        }
    }
}