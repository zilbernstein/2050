package ru.digipeople.message.ui.activity.writemessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.message.MessagesComponent
import ru.digipeople.message.R
import ru.digipeople.message.model.Contact
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.message.ui.activity.writemessage.adapter.AddresseeAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити отправки сообщения
 */
class WriteMessageActivity : MvpActivity(), WriteMessageView {
    //region DI
    private lateinit var screenComponent: WriteMessageScreenComponent
    private lateinit var component: WriteMessageComponent
    @Inject
    lateinit var messageActivityNavigator: MessageActivityNavigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    //endRegion
    //region View
    private lateinit var text: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var addresseeImage: View
    //endRegion
    //region Other
    private val adapter = AddresseeAdapter()
    private lateinit var presenter: WriteMessagePresenter
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = CoreAppComponent.get().screenOrientation()
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_message)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.write_message_activity_title)
        toolbarDelegate.setNavigationIcon(ru.digipeople.locotech.core.R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, WriteMessagePresenter::class.java)
        presenter.initialize()

        addresseeImage = findViewById(R.id.activity_write_message_add_icon)
        /**
         * Обработка нажатия на добавить адресата
         */
        addresseeImage.setOnClickListener { presenter.onAddresseeImageClicked() }

        recyclerView = findViewById(R.id.activity_write_message_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.write_message_addressee_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.adapter = adapter

        adapter.onDeleteItemClickListener = presenter::onDeleteAddresseeButtonClicked

        text = findViewById(R.id.activity_write_message_text)
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.write_message, menu)
        return true
    }
    /**
     * Обработка пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send -> {
                presenter.onSendButtonClicked(text.text.toString())
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * переход назад
     */
    override fun navigateBack() {
        messageActivityNavigator.navigateBack(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.onScreenShown()
    }
    /**
     * переход к поиску адресатов
     */
    override fun navigateToSearchAddressee() {
        messageActivityNavigator.navigateToChooseAddresseActivity(this)
    }
    /**
     * ошибка пустого списка адресатов
     */
    override fun showEmptyAddresseeMistake() {
        Snackbar.make(recyclerView, R.string.wrtite_message_activity_empty_addressee_mistake, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Ошибка пустого сообщения
     */
    override fun showEmptyTextMistake() {
        Snackbar.make(recyclerView, R.string.wrtite_message_activity_empty_message_mistake, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * отображение ошибки
     */
    override fun showError(userError: UserError) {
        Snackbar.make(recyclerView, userError.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * установка текста
     */
    override fun setText(text: String) {
        this.text.setText(text)
    }
    /**
     * установка адресатов
     */
    override fun setAddressees(addressees: List<Contact>) {
        adapter.items = addressees
    }

    private fun getScreenComponent(): WriteMessageScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return MessagesComponent.get().writeMessageScreenComponent()
        } else {
            return saved as WriteMessageScreenComponent
        }

    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, WriteMessageActivity::class.java)
        }

    }
}
