package ru.digipeople.locotech.master.ui.activity.tmcsearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.TMC
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmcsearch.adapter.TmcSearchAdapter
import ru.digipeople.qrscanner.ui.activity.scanner.ScannerActivity
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.DebounceQueryTextListener
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcSearchActivity : MvpActivity(), TmcSearchView {
    //region DI
    private lateinit var screenComponent: TmcSearchScreenComponent
    private lateinit var component: TmcSearchComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: TmcSearchAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //region View
    private lateinit var backBtn: ImageView
    private lateinit var search: SearchView
    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var dataGroup: Group
    private lateinit var noDataMsg: TextView
    private lateinit var inStock: CheckBox
    private lateinit var scanBtn: ImageView
    // end Region
    //region Other
    private lateinit var presenter: TmcSearchPresenter
    private lateinit var workId: String
    private lateinit var tmcIdList: ArrayList<String>
    //end Region
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        workId = intent.getStringExtra(EXTRA_WORK_ID)
        tmcIdList = intent.getStringArrayListExtra(EXTRA_TMC_ID_LIST)
        screenComponent = getScreenComponent()
        component = screenComponent.component()
                .activityModule(ActivityModule(this))
                .workId(workId)
                .tmcIdList(tmcIdList)
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmc_search)

        inStock = findViewById(R.id.in_stock)
        inStock.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.onInStockCheckedChange(isChecked)
        }
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, TmcSearchPresenter::class.java)
        presenter.initialize()

        backBtn = findViewById(R.id.back_button)
        /**
         * Обработка нажатия назад
         */
        backBtn.setOnClickListener { presenter.onBackBtnClicked() }

        search = findViewById(R.id.search_view)
        /**
         * Обработка ввода строки поиска
         */
        search.setOnQueryTextListener(DebounceQueryTextListener(1000,
                textChangeListener = presenter::onQueryStrChanged,
                querySubmitListener = presenter::onQueryStrChanged))

        scanBtn = findViewById(R.id.scan_view)
        scanBtn.setOnClickListener { presenter.onScanBtnClicked(TAKE_QR_REQUEST_CODE) }

        recycler = findViewById(R.id.recycler_view)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.onItemClickListener = presenter::itemClicked

        dataGroup = findViewById(R.id.info_group)
        noDataMsg = findViewById(R.id.no_data)
    }
    /**
     * Действия при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
        presenter.onScreenShown()
    }
    /**
     * Действия при приостановке активити
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        // nop
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установка значений
     */
    override fun setItems(items: List<TMC>) {
        adapter.items = items
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * Отображение сообщения об отсутствии данных
     */
    override fun setNoDataMsgVisible(visible: Boolean) {
        noDataMsg.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * Управление видимостью заголовка
     */
    override fun setHeaderVisible(visible: Boolean) {
        dataGroup.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(error: UserError) {
        Snackbar.make(recycler, error.message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): TmcSearchScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().tmcSearchScreenComponent()
        } else {
            return saved as TmcSearchScreenComponent
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            /**
             * Получение строки поиска из интента
             */
            TAKE_QR_REQUEST_CODE -> {
                val text = ScannerActivity.getResultFromIntent(resultCode, data)
                search.setQuery(text, true)
            }
        }
    }

    companion object {
        //region Extra
        private const val EXTRA_WORK_ID = "EXTRA_WORK_ID"
        private const val EXTRA_TMC_ID_LIST = "EXTRA_TMC_ID_LIST"
        private const val TAKE_QR_REQUEST_CODE = 103

        //end region
        fun getCallingIntent(context: Context, workId: String, tmcIdList: ArrayList<String>): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, TmcSearchActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            intent.putStringArrayListExtra(EXTRA_TMC_ID_LIST, tmcIdList)
            return intent
        }
    }
}