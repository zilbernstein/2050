package ru.digipeople.telephonebook.ui.activity.telephone

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import com.volcaniccoder.volxfastscroll.Volx
import ru.digipeople.telephonebook.R
import ru.digipeople.telephonebook.TelephoneBookComponent
import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.telephonebook.ui.Navigator
import ru.digipeople.telephonebook.ui.activity.telephone.adapter.DiffUtilCallback
import ru.digipeople.telephonebook.ui.activity.telephone.adapter.TelephoneBookAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.CallPhoneRtPermissionDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.input.Keyboard
import javax.inject.Inject

/**
 * Активити телефонный справочник
 *
 * @author Sostavkin Grisha
 */
class TelephoneBookActivity : MvpActivity(), TelephoneBookView {

    //region DI
    private lateinit var screenComponent: TelephoneBookScreenComponent
    private lateinit var component: ru.digipeople.telephonebook.ui.activity.telephone.TelephoneBookComponent
    @Inject
    lateinit var phoneCallPermissionDelegate: CallPhoneRtPermissionDelegate
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var contactAdapter: TelephoneBookAdapter
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
    private lateinit var presenter: TelephoneBookPresenter
    //endRegion
    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telephone_book)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.telephoneBookPresenter() }, TelephoneBookPresenter::class.java)
        presenter.initialize()
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.telephone_book_activity)

        phoneCallPermissionDelegate.onCreate(savedInstanceState)
        /**
         * проверка разрешения
         */
        phoneCallPermissionDelegate.setCallback { granted -> presenter.onPermissionChanged(granted) }

        searchView = findViewById(R.id.activity_telephone_book_search_view)
        frameLayout = findViewById(R.id.activity_telephone_book_frame)

        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, linearLayoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)

        recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.activity_telephone_book_recycler).apply {
            layoutManager = linearLayoutManager
            adapter = contactAdapter
            addItemDecoration(dividerItemDecoration)
        }

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

        //Активируем searchView при нажатии на любую область
        searchView.setOnClickListener { searchView.onActionViewExpanded() }
        searchView.setOnQueryTextListener(searchListener)

        contactAdapter.itemClickListener = presenter::callClicked
        /**
         * инициализация бокового меню
         */
        mainDrawerDelegate.init(false)
    }
    /**
     * проверка разрешения
     */
    override fun checkPermissions() {
        phoneCallPermissionDelegate.requestPermissions()
    }

    override fun onStart() {
        super.onStart()
        presenter.onScreenShow()
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
        phoneCallPermissionDelegate.onResume()
    }

    override fun onPause() {
        navigator.onPause()
        phoneCallPermissionDelegate.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        phoneCallPermissionDelegate.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        phoneCallPermissionDelegate.onSaveInstanceState(outState)
    }
    /**
     * Получения результата запроса разрешения
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        phoneCallPermissionDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        contactAdapter.notifyDataSetChanged()
        if (list.isNullOrEmpty()) {
            frameLayout.visibility = View.INVISIBLE
        } else {
            frameLayout.visibility = View.VISIBLE
            val diffUtilCallback = DiffUtilCallback(contactAdapter.items, list)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
            contactAdapter.items = list
            diffResult.dispatchUpdatesTo(contactAdapter)
        }
        volx.notifyValueDataChanged()
    }
    /**
     * управление видимостью лоадера
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

    override fun notifyVolxDataChanged() {
        volx.notifyValueDataChanged()
    }

    private fun getScreenComponent(): TelephoneBookScreenComponent {
        return TelephoneBookComponent.get().telephoneBookDetailsScreenComponent()
    }
    /**
     * обработка изменения строки поиска
     */
    private val searchListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            presenter.searchSubmitted(query)
            Keyboard.hide(searchView)
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            presenter.searchChanged(newText)
            return true
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            /**
             * интент
             */
            val intent = Intent(context, TelephoneBookActivity::class.java)
            return intent
        }
    }
}
