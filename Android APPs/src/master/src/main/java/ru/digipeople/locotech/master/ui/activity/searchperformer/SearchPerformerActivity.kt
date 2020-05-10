package ru.digipeople.locotech.master.ui.activity.searchperformer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.PerformerItem
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.searchperformer.adapter.AddedPerformerAdapter
import ru.digipeople.locotech.master.ui.activity.searchperformer.adapter.DiffUtilCallback
import ru.digipeople.locotech.master.ui.activity.searchperformer.adapter.SearchPerformerAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.DebounceQueryTextListener
import ru.digipeople.utils.DateUtils
import ru.digipeople.utils.input.Keyboard
import javax.inject.Inject

/**
 * Активити выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
class SearchPerformerActivity : MvpActivity(), SearchPerformerView {
    //region const
    //end Region
    //region DI
    private lateinit var screenComponent: SearchPerformerScreenComponent
    private lateinit var component: SearchPerformerComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: SearchPerformerAdapter
    @Inject
    lateinit var addedAdapter: AddedPerformerAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //region other
    //region View
    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var addedRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var workTitle: TextView
    private lateinit var outfitTime: TextView
    private lateinit var outfitNormal: TextView
    private lateinit var outfitTitle: TextView
    private lateinit var parentLayout: androidx.drawerlayout.widget.DrawerLayout
    //endRegion
    //region Other
    private lateinit var presenter: SearchPerformerPresenter
    var workId: Int? = null
    var callingId: Int? = null
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .params(intent.getParcelableExtra(EXTRA_PARAMS))
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performer_search)

        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, SearchPerformerPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        /**
         * Установка заголовка
         */
        if (callingId == CALLING_WORK_LIST) {
            toolbarDelegate.setTitle(R.string.search_performer_title)
        } else {
            toolbarDelegate.setTitle(R.string.search_performer_title_perf)
        }

        workTitle = findViewById(R.id.work_title)
        outfitTime = findViewById(R.id.outfit_time)
        outfitNormal = findViewById(R.id.outfit_normal)
        outfitTitle = findViewById(R.id.outfit_title)

        searchView = findViewById(R.id.activity_performer_search_view)
        //Активируем searchView при нажатии на любую область
        searchView.setOnClickListener { searchView.onActionViewExpanded() }
        searchView.setOnQueryTextListener(DebounceQueryTextListener(1000,
                textChangeListener = presenter::searchChanged,
                querySubmitListener = {
                    presenter::searchChanged
                    Keyboard.hide(searchView)
                }))

        parentLayout = findViewById(R.id.drawerLayout)
        /**
         * Инициализация ресайклера
         */
        searchRecyclerView = findViewById(R.id.activity_performer_search_recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        searchRecyclerView.layoutManager = layoutManager

        addedRecyclerView = findViewById(R.id.added_performer_recycler_view)
        val addedLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        addedRecyclerView.layoutManager = addedLayoutManager

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        searchRecyclerView.addItemDecoration(dividerItemDecoration)
        addedRecyclerView.addItemDecoration(dividerItemDecoration)

        adapter.itemClickListener = presenter::itemAddClicked
        addedAdapter.onItemClickListener = presenter::itemRemoveClicked
        searchRecyclerView.adapter = adapter
        addedRecyclerView.adapter = addedAdapter
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(true)
    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действия при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при постановке активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
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
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_with_check, menu)
        return true
    }
    /**
     * Добавление пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.check -> {
                presenter.addPerformerClicked()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    /**
     * Установка исполнителей
     */
    override fun setWorkers(list: List<PerformerItem>) {
        val diffUtilCallback = DiffUtilCallback(adapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        adapter.items = list
        diffResult.dispatchUpdatesTo(adapter)
    }
    /**
     * Установка исполниетелей в работу
     */
    override fun setWorkersInWork(list: List<PerformerItem>) {
        val diffUtilCallback = DiffUtilCallback(addedAdapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        addedAdapter.items = list
        diffResult.dispatchUpdatesTo(addedAdapter)
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Отображение ошибки перенагрузки исполниетелей
     */
    override fun showOverLoadError() {
        showError(getString(R.string.search_performer_overload_mistake))
    }
    /**
     * Отображение ошибки не установленной продолжительности смены
     */
    override fun showNullShiftError() {
        showError(getString(R.string.search_performer_null_shift_mistake))
    }
    /**
     * Оображение данных
     */
    override fun showWorkModel(work: Work) {
        workTitle.text = work.title
        outfitNormal.text = getString(R.string.search_performer_outfit_normal, DateUtils.convertToString(work.normalTime))
        outfitTitle.text = getString(R.string.search_performer_outfit_title, work.outfitNumber)
        outfitTime.text = getString(R.string.search_performer_outfit_time, DateUtils.convertToString(work.remainTime), DateUtils.convertToString((work.workPartPercent * work.normalTime / 100)), work.workPartPercent)

    }

    private fun getScreenComponent(): SearchPerformerScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().searchPerformerScreenComponent()
        } else {
            return saved as SearchPerformerScreenComponent
        }

    }

    companion object {
        //region Extras
        private val EXTRA_PARAMS = "EXTRA_PARAMS"
        private val EXTRA_CALLING_ID = "EXTRA_CALLING_ID"
        val CALLING_WORK_LIST = 0
        val CALLING_LOCOMOTIVE_DETAIL = 1
        //endregion

        fun getCallingIntent(context: Context, params: SearchPerformerParams, callingId: Int): Intent {
            /**
             * интент
             */
            val intent = Intent(context, SearchPerformerActivity::class.java)
            intent.putExtra(EXTRA_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, callingId)
            return intent
        }

    }
}