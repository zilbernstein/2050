package ru.digipeople.locotech.inspector.ui.activity.remarksearch

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
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.RemarkFromCatalog
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.createremarkphoto.CreateRemarkPhotoActivity
import ru.digipeople.locotech.inspector.ui.activity.inspection.StartTab
import ru.digipeople.locotech.inspector.ui.activity.remarksearch.adapter.DiffUtilCallback
import ru.digipeople.locotech.inspector.ui.activity.remarksearch.adapter.RemarkSearchAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.DebounceQueryTextListener
import javax.inject.Inject

/**
 * Активити добавления/выбора замечаний
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
    //regionOthers
    private var startTab = 0

    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remark_search)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, RemarkSearchPresenter::class.java)
        presenter.initialize()
        /**
         * инициализация тулбара
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
        customRemarkBtn.setOnClickListener { presenter.onCustomRemarkClicked(searchView.query.toString()) }

        recyclerView = findViewById(R.id.activity_remark_search_recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter.itemClickListener = presenter::itemClicked
        recyclerView.adapter = adapter

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        startTab = intent.getIntExtra(EXTRA_START_TYPE, StartTab.CYCLIC_WORK.code)
        presenter.setStartTab(startTab)
        /**
         * инициализация бокового меню
         */
        mainDrawerDelegate.init(true)
    }
    /**
     * переход назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
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
     * обновление данных в адаптере
     */
    override fun updateAdapter(list: List<RemarkFromCatalog>) {
        val diffUtilCallback = DiffUtilCallback(adapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        adapter.items = list
        diffResult.dispatchUpdatesTo(adapter)
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            START_PHOTO_REQUEST -> {
                val isSuccesfull = CreateRemarkPhotoActivity.getResultFromIntent(resultCode)
                if (isSuccesfull) {
                    presenter.onResultReturned()
                }
            }
        }
    }


    private fun getScreenComponent(): RemarkSearchScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().remarkSearchScreenComponent()
        } else {
            return saved as RemarkSearchScreenComponent
        }

    }

    override fun dismissCustomRemarkDialog() {
        dialog?.dismiss()
    }

    override fun showRemarkAlreadyAddedError() {
        Snackbar.make(recyclerView, R.string.remark_search_already_added, Snackbar.LENGTH_SHORT).show()
    }

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

    override fun setLoadingVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }

    override fun showEmptyNameError() {
        showError(getString(R.string.remark_search_empty_remark_error))
    }

    companion object {
        const val START_PHOTO_REQUEST = 100
        private const val EXTRA_START_TYPE = "EXTRA_START_TYPE"

        fun getCallingIntent(context: Context, startTab: Int): Intent {
            val intent = Intent(context, RemarkSearchActivity::class.java)
            intent.putExtra(EXTRA_START_TYPE, startTab)
            return intent
        }

    }
}