package ru.digipeople.locotech.inspector.ui.activity.repairbook

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.Document
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.repairbook.adapter.DocumentsAdapter
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.HttpUtils
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Активити книги ремонтов
 */
class RepairBookActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerRepairBookComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .coreAppComponent(CoreAppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyDataText: TextView
    private lateinit var noData: View
    //endregion
    //region Other
    private lateinit var viewModel: RepairBookViewModel
    private val webViewHeaders = mutableMapOf<String, String>()
    private val adapter = DocumentsAdapter()
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repair_book)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        /**
         * инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        progressBar = findViewById(R.id.progress_bar)
        emptyDataText = findViewById(R.id.empty_data)
        noData = findViewById(R.id.noDataView)

        webView = findViewById(R.id.web_view)
        val webSettings = webView.settings
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webView.webViewClient = webViewClient

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(RepairBookViewModel::class.java)
        viewModel.apply {
            equipmentNameLiveData.observe({ lifecycle }, ::setEquipmentName)
            documentsLiveData.observe({ lifecycle }, ::setDocuments)
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            selectedDocumentLiveData.observe({ lifecycle }, ::setSelectedDocument)
            authInfoLiveData.observe({ lifecycle }, ::setAuthInfo)

            start()
        }

        adapter.onItemClickListener = viewModel::onDocumentClicked
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }

    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * переход назад
     */
    override fun onBackPressed() {
        // nop
    }
    /**
     * отображнеи сообщения нет данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        noData.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * отображнеи ошибки
     */
    private fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * установка наименования оборудования
     */
    private fun setEquipmentName(equipmentName: String) {
        toolbarDelegate.setTitle(getString(R.string.repair_book_activity_title, equipmentName))
    }
    /**
     * установка документов
     */
    private fun setDocuments(documents: List<Document>) {
        adapter.items = documents
    }
    /**
     * установка выбранных документов
     */
    private fun setSelectedDocument(document: Document) {
        adapter.selectedItemId = document.id
        if (document.url.isBlank()) {
            webView.loadUrl("about:blank")
            emptyDataText.visibility = View.VISIBLE
        } else {
            webView.loadUrl(document.url, webViewHeaders)
            emptyDataText.visibility = View.GONE
        }
    }
    /**
     * установка данных авторизации
     */
    private fun setAuthInfo(usernamePasswordPair: Pair<String, String>) {
        val header = HttpUtils.buildBasicAuthHeader(
                usernamePasswordPair.first,
                usernamePasswordPair.second
        )
        webViewHeaders["Authorization"] = header
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, RepairBookActivity::class.java)
        }
    }
}