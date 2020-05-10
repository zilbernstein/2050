package ru.digipeople.locotech.master.ui.activity.remarksearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.RemarkFromCatalog
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.remarksearch.adapter.DiffUtilCallback
import ru.digipeople.locotech.master.ui.activity.remarksearch.adapter.RemarkSearchAdapter
import ru.digipeople.locotech.master.ui.activity.remarksearch.dialog.CustomRemarkDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.DebounceQueryTextListener
import javax.inject.Inject


/**
 * Активити добавления / создания замечания
 *
 * @author Kashonkov Nikita
 */
class RemarkSearchActivity : MvpActivity(), RemarkSearchView {
    //region DI
    private lateinit var screenComponent: RemarkSearchScreenComponent
    private lateinit var component: RemarkSearchComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: RemarkSearchAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //region View
    private lateinit var parentLayout: androidx.drawerlayout.widget.DrawerLayout
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var customRemarkBtn: ImageButton
    private var dialog: CustomRemarkDialog? = null
    //endRegion
    //regionView
    private lateinit var presenter: RemarkSearchPresenter
    //endRegion

    /**
     * Действия пр  создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remark_search)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, RemarkSearchPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.remark_search_title)

        searchView = findViewById(R.id.activity_remark_search_view)
        searchView.setOnQueryTextListener(DebounceQueryTextListener(1000,
                textChangeListener = presenter::searchSubmitted,
                querySubmitListener = presenter::searchSubmitted))
        //Активируем searchView при нажатии на любую область
        searchView.setOnClickListener { searchView.onActionViewExpanded() }
        //Ограничиваем максимально возможную длину вводимого замечания
        val et = searchView.findViewById(R.id.search_src_text) as TextView
        et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(100))

        parentLayout = findViewById(R.id.drawerLayout)

        customRemarkBtn = findViewById(R.id.activity_remark_search_add)
        customRemarkBtn.setOnClickListener { presenter.customRemarkClicked(searchView.query.toString()) }
        /**
         * Инициализация ресакйклера
         */
        recyclerView = findViewById(R.id.activity_remark_search_recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter.itemClickListener = presenter::itemClicked
        recyclerView.adapter = adapter
        /**
         * оформление элементов списка
         */
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(true)
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
     * Действия при переходе активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * Обновление данных в адаптере
     */
    override fun updateAdapter(list: List<RemarkFromCatalog>) {
        val diffUtilCallback = DiffUtilCallback(adapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        adapter.items = list
        diffResult.dispatchUpdatesTo(adapter)
    }
    /**
     * Отображение ошибок
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): RemarkSearchScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().remarkSearchScreenComponent()
        } else {
            return saved as RemarkSearchScreenComponent
        }
    }
    /**
     * Отмена диалога
     */
    override fun dismissCustomRemarkDialog() {
        dialog?.dismiss()
    }
    /**
     * Отображение диалога
     */
    override fun showCustomRemarkDialog(text: String) {
        if (dialog != null) {
            return
        }

        dialog = CustomRemarkDialog(this)
        dialog?.agreeListener = presenter::customRemarkCreate
        dialog?.cancelListener = presenter::customRemarkCancel
        dialog?.titleText = text
        dialog?.setOnDismissListener { dialog = null }
        dialog?.show()
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Сообщение об ошибке пустого имени
     */
    override fun showEmptyNameError() {
        showError(getString(R.string.remark_search_empty_remark_error))
    }

    companion object {
        /**
         * интент
         */
        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, RemarkSearchActivity::class.java)
            return intent
        }

    }
}