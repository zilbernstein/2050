package ru.digipeople.locotech.master.ui.activity.allworklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.WorkFromCatalog
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.allworklist.adapter.AllWorkAdapter
import ru.digipeople.locotech.master.ui.activity.allworklist.adapter.DiffUtilCallback
import ru.digipeople.locotech.master.ui.activity.allworklist.countdialog.CountDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.DebounceQueryTextListener
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити списка работ
 * @author Kashonkov Nikita
 */
class AllWorkActivity : MvpActivity(), AllWorkView {
    //region DI
    private lateinit var screenComponent: AllWorkScreenComponent
    private lateinit var component: AllWorkComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: AllWorkAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //region View
    private lateinit var backImage: ImageView
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var noDataMsg: TextView
    // endregion
    // region Other
    private lateinit var presenter: AllWorkPresenter
    private lateinit var remarkId: String
    private var callingType = 0
    private var countDialog: CountDialog? = null
    //endRegion
    /**
     * Функционал, выполняющийся при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        callingType = intent.getIntExtra(EXTRA_CALLING_ID, 0)
        remarkId = intent.getStringExtra(EXTRA_REMARK_ID)
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .remarkId(remarkId)
                .callingId(callingType)
                .build()

        component.inject(this)

        super.onCreate(savedInstanceState)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, AllWorkPresenter::class.java)
        presenter.initialize()
        /**
         * Установка вью
         */
        setContentView(R.layout.activity_all_work)
        /**
         * Инициализация поиска
         */
        searchView = findViewById(R.id.activity_all_work_search_view)
        searchView.setOnQueryTextListener(DebounceQueryTextListener(1000,
                textChangeListener = presenter::onQueryStrChanged,
                querySubmitListener = presenter::onQueryStrChanged))
        /**
         * Инициализация recyclerView
         */
        recyclerView = findViewById(R.id.activity_all_work_recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter.itemClickListener = presenter::itemClicked
        recyclerView.adapter = adapter

        backImage = findViewById(R.id.activity_comment_back_button)
        backImage.setOnClickListener { presenter.backButtonClicked() }

        noDataMsg = findViewById(R.id.empty_data_splash)

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }
    /**
     * Методы выполняемые при каждом старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действия, выполняемые при нажатии кнопки назад
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }
    /**
     * Методы. выполняемые при возобновлении работы активити
     */
    override fun onResume() {
        navigator.onResume(this)
        super.onResume()
    }
    /**
     * Методы. выполняемые при приостановке работы активити
     */
    override fun onPause() {
        navigator.onPause()
        countDialog?.dismiss()
        countDialog = null
        loadingFragmentDelegate.setLoadingVisibility(false)
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Функция обновления данных в адаптере
     */
    override fun updateAdapter() {
        adapter.notifyDataSetChanged()
    }
    /**
     * Функция управляющая видимостью лоадера
     */
    override fun setProgressVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Методы. выполняемые при возобновлении работы активити
     */
    override fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Загрузка данных в адаптер
     */
    override fun setDataToAdapter(list: List<WorkFromCatalog>) {
        val diffUtilCallback = DiffUtilCallback(adapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        adapter.items = list
        diffResult.dispatchUpdatesTo(adapter)
    }
    /**
     * Вызов диалога с запросом о количестве повторов
     */
    override fun setCountDialogVisibility(isVisible: Boolean) {
        if (isVisible) {
            if (countDialog == null) {
                countDialog = CountDialog().apply {
                    onDismissListener = { countDialog = null }
                    onCountClickListener = presenter::onRepeatsCountChoose
                }
                supportFragmentManager.beginTransaction().add(countDialog!!, null).commit()
            }
        } else {
            countDialog?.dismiss()
            countDialog = null
        }
    }
    /**
     * Функция, управляющая отображением сообщения об отсутствии данных
     */
    override fun setEmptyDataSplashVisibility(isVisible: Boolean) {
        /*if (isVisible) {
            recyclerView.visibility = View.GONE
            noDataMsg.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noDataMsg.visibility = View.GONE
        }*/
        noDataMsg.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getScreenComponent(): AllWorkScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().allWorkScreenComponent()
        } else {
            return saved as AllWorkScreenComponent
        }
    }

    companion object {
        //region Extras
        private const val EXTRA_CALLING_ID = "EXTRA_CALLING_ID"
        private const val EXTRA_REMARK_ID = "EXTRA_REMARK_ID"
        const val CALLING_CUSTOM_REMARK = 1
        //endregion
        /**
         * Функция, формирующая базовый интент
         */
        fun getCallingIntent(context: Context, remarkId: String): Intent {
            val intent = Intent(context, AllWorkActivity::class.java)
            intent.putExtra(EXTRA_REMARK_ID, remarkId)
            return intent
        }
        /**
         * Функция, формирующая интент для замечания
         */
        fun getCallingIntentForCustomRemark(context: Context, remarkId: String): Intent {
            val intent = Intent(context, AllWorkActivity::class.java)
            intent.putExtra(EXTRA_CALLING_ID, CALLING_CUSTOM_REMARK)
            intent.putExtra(EXTRA_REMARK_ID, remarkId)
            return intent
        }
    }
}