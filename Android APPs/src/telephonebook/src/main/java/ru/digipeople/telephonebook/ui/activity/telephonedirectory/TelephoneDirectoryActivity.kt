package ru.digipeople.telephonebook.ui.activity.telephonedirectory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.volcaniccoder.volxfastscroll.Volx
import ru.digipeople.telephonebook.R
import ru.digipeople.telephonebook.TelephoneBookComponent
import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.telephonebook.ui.Navigator
import ru.digipeople.telephonebook.ui.activity.telephonedirectory.adapter.TelephoneDirectoryAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити телефонный справочник
 *
 * @author Sostavkin Grisha
 */
class TelephoneDirectoryActivity : MvpActivity(), TelephoneDirectoryView {

    //region DI
    private lateinit var screenComponent: TelephoneDirectoryScreenComponent
    private lateinit var component: TelephoneDirectoryComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: TelephoneDirectoryAdapter
    @Inject
    lateinit var loadingFragment: LoadingFragmentDelegate
    //endRegion
    //region View
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var frameLayout: FrameLayout
    private lateinit var volx: Volx
    //endRegion
    //region Other
    private lateinit var presenter: TelephoneDirectoryPresenter
    //endRegion
    /**
     * действия при созданиии активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telephone_directory)
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.telephone_book_activity)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.telephoneDirectoryPresenter() }, TelephoneDirectoryPresenter::class.java)
        presenter.initialize()

        frameLayout = findViewById(R.id.activity_telephone_book_frame)
        searchView = findViewById(R.id.activity_telephone_book_search_view)

        recyclerView = findViewById(R.id.activity_telephone_book_recycler)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter.itemClickListener = presenter::callClicked


        //Активируем searchView при нажатии на любую область
        searchView.setOnClickListener { searchView.onActionViewExpanded() }
        searchView.setOnQueryTextListener(searchListener)

        //Для быстрого поиска по букве
        volx = Volx.Builder()
                .setUserRecyclerView(recyclerView)
                .setParentLayout(frameLayout)
                .setTextColor(Color.GRAY)
                .setBackgroundColor(Color.WHITE)
                .setRightStrokeColor(Color.WHITE)
                .setActiveColor(Color.DKGRAY)
                .setMiddleStrokeColor(Color.WHITE)
                .setMiddleBackgroundColor(Color.GRAY)
                .setMiddleTextColor(Color.WHITE)
                .setDelayMillis(Volx.NEVER_CLOSE)
                .build()
        /**
         * инициализация бокового меню
         */
        mainDrawerDelegate.init(false)
    }
    /**
     * действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        Volx.NEVER_CLOSE
        presenter.onScreenShow()
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
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
    }
    /**
     * установка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Contact>) {
        recyclerView.adapter = adapter
        adapter.dataSet = list
        adapter.setData(list)
        volx.notifyValueDataChanged()
    }
    /**
     * обработка видимости лоадера
     */
    override fun setLoading(isLoading: Boolean) {
        loadingFragment.setLoadingVisibility(isLoading)
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(frameLayout, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    private fun getScreenComponent(): TelephoneDirectoryScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return TelephoneBookComponent.get().telephoneDirectoryScreenComponent()
        } else {
            return saved as TelephoneDirectoryScreenComponent
        }
    }
    /**
     * обработка изменения строки поиска
     */
    private val searchListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            presenter.searchSubmitted(query!!)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            presenter.searchChanged(newText!!)
            return true
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            /**
             * интент
             */
            val intent = Intent(context, TelephoneDirectoryActivity::class.java)
            return intent
        }
    }
}