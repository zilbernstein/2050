package ru.digipeople.locotech.inspector.ui.activity.signersearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.signersearch.adapter.SignerModel
import ru.digipeople.locotech.inspector.ui.activity.signersearch.adapter.SignersAdapter
import ru.digipeople.locotech.master.ui.activity.searchperformer.SignerSearchScreenComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.DebounceQueryTextListener
import javax.inject.Inject

/**
 * Активити поиска подписантов
 *
 * @author Kashonkov Nikita
 */
class SignerSearchActivity : MvpActivity(), SignerSearchView {
    //region DI
    private lateinit var screenComponent: SignerSearchScreenComponent
    private lateinit var component: SignerSearchComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: SignersAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //region other
    //region View
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: DrawerLayout
    private lateinit var applyBtn: Button
    //endRegion
    //region Other
    private lateinit var presenter: SignerSearchPresenter
    var workId: Int? = null
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signer_search)
        /**
         * инициализация делегата
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.signer_search_activity_title)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, SignerSearchPresenter::class.java)
        presenter.initialize()

        searchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(DebounceQueryTextListener(1000,
                textChangeListener = presenter::searchSubmitted,
                querySubmitListener = presenter::searchSubmitted))
        //Активируем searchView при нажатии на любую область
        searchView.setOnClickListener { searchView.onActionViewExpanded() }

        parentLayout = findViewById(R.id.drawerLayout)

        recyclerView = findViewById(R.id.recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter.itemClickListener = presenter::itemClicked
        recyclerView.adapter = adapter
        /**
         * инициализация бокового меню
         */
        mainDrawerDelegate.init(true)

        applyBtn = findViewById(R.id.apply_btn)
        applyBtn.setOnClickListener { presenter.onApplyBtnClicked() }
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
     * переход назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * установка данных
     */
    override fun setData(list: List<SignerModel>) {
        adapter.items = list
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): SignerSearchScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().signerSearchScreeenComponent()
        } else {
            return saved as SignerSearchScreenComponent
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            /**
             * интент
             */
            val intent = Intent(context, SignerSearchActivity::class.java)
            return intent
        }
    }
}