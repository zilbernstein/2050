package ru.digipeople.message.ui.activity.chooseaddressee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.message.MessagesComponent
import ru.digipeople.message.R
import ru.digipeople.message.model.Contact
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.message.ui.activity.chooseaddressee.adapter.ChooseAddresseeAdapter
import ru.digipeople.message.ui.activity.chooseaddressee.adapter.DiffUtilCallback
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.view.DebounceQueryTextListener
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити выбора адресата
 *
 * @author Kashonkov Nikita
 */
class ChooseAddresseActivity : MvpActivity(), ChooseAddresseeView {
    //region DI
    private lateinit var screenComponent: ChooseAddresseeScreenComponent
    private lateinit var component: ChooseAddresseComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var messageActivityNavigator: MessageActivityNavigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //region View
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var addPerformerBtn: Button
    //endRegion
    //region Other
    private lateinit var presenter: ChooseAddresseePresenter
    private val adapter = ChooseAddresseeAdapter()
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_addressee)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, ChooseAddresseePresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.choose_addressee_title)

        searchView = findViewById(R.id.choose_addressee_search_view)
        searchView.setOnQueryTextListener(DebounceQueryTextListener(1000,
                textChangeListener = presenter::searchChanged,
                querySubmitListener = presenter::searchChanged))
        //Активируем searchView при нажатии на любую область
        searchView.setOnClickListener { searchView.onActionViewExpanded() }

        addPerformerBtn = findViewById(R.id.choose_addresee_search_add_addresee)
        /**
         * обработка добавления получателя
         */
        addPerformerBtn.setOnClickListener { presenter.addAddresseeClicked() }
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(true)
        mainDrawerDelegate.toolbarDelegate.setNavigationOnClickListener {
            onBackPressed()
        }

        recyclerView = findViewById(R.id.choose_addressee_search_recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter.onItemClickListener = presenter::itemClicked
        recyclerView.adapter = adapter
    }
    /**
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * переход на предыдущий экран
     */
    override fun navigateBack() {
        messageActivityNavigator.navigateBack(this)
    }
    /**
     * Управление видимсотью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showUserError(userError: UserError) {
        Snackbar.make(recyclerView, userError.message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): ChooseAddresseeScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return MessagesComponent.get().chooseAddresseeScreenComponent()
        } else {
            return saved as ChooseAddresseeScreenComponent
        }
    }
    /**
     * Установка данных
     */
    override fun setData(contacts: List<Contact>) {
        val diffUtilCallback = DiffUtilCallback(adapter.items, contacts)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        adapter.items = contacts
        diffResult.dispatchUpdatesTo(adapter)
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ChooseAddresseActivity::class.java)
        }

    }
}