package ru.digipeople.locotech.master.ui.activity.worklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.worklist.adapter.DiffUtilCallback
import ru.digipeople.locotech.master.ui.activity.worklist.adapter.WorkListAdapter
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити добавление замечания / работ
 *
 * @author Kashonkov Nikita
 */
class WorkListActivity : MvpActivity(), WorkListView {
    //region DI
    private lateinit var screenComponent: WorkListScreenComponent
    private lateinit var component: WorkListComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: WorkListAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //region other
    //region Other
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var backBtn: Button
    private lateinit var addRemarkBtn: Button
    private lateinit var presenter: WorkListPresenter
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val remarkId = intent.getStringExtra(EXTRA_REMARK_ID)
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .remarkId(remarkId)
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_list)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.work_list_title)

        backBtn = findViewById(R.id.activity_work_list_back_btn)
        backBtn.setOnClickListener { presenter.backButtonClicked() }

        addRemarkBtn = findViewById(R.id.activity_work_list_add_remark)
        addRemarkBtn.setOnClickListener { presenter.addRemarkButtonClicked() }
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, WorkListPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(true)

        recyclerView = findViewById(R.id.activity_work_list_recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.adapter = adapter
    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действия при восстановлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при приостановке активити
     */
    override fun onPause() {
        super.onPause()
        navigator.onPause()
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Work>) {
        val diffUtilCallback = DiffUtilCallback(adapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        adapter.items = list
        diffResult.dispatchUpdatesTo(adapter)
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): WorkListScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().workListScreenComponent()
        } else {
            return saved as WorkListScreenComponent
        }

    }

    companion object {
        //region Extras
        private val EXTRA_REMARK_ID = "EXTRA_REMARK_ID"
        //endregion

        fun getCallingIntent(context: Context, remarkId: String): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, WorkListActivity::class.java)
            intent.putExtra(EXTRA_REMARK_ID, remarkId)
            return intent
        }
    }
}
