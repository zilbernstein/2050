package ru.digipeople.message.ui.activity.messagelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import ru.digipeople.message.MessagesComponent
import ru.digipeople.message.R
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.message.ui.fragment.messagelist.MessageListFragment
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import javax.inject.Inject

/**
 * Активити списка сообщений
 *
 * @author Kashonkov Nikita
 */
class MessageListActivity : MvpActivity(), MessageListView {
    //region DI
    private lateinit var screenComponent: MessageListScreenComponent
    private lateinit var component: MessageListComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var messageActivityNavigator: MessageActivityNavigator
    //endRegion
    //region View
    private lateinit var container: FrameLayout
    //endRegion
    //region Other
    private lateinit var presenter: MessageListPresenter
    private lateinit var fragment: MessageListFragment
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.message_activity_title)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, MessageListPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        container = findViewById(R.id.activity_message_list_container)

        fragment = MessageListFragment.newInstance()
        fragment.onMessageClickListener = presenter::onMessageClicked
        fragment.onCreateMessageClickListener = presenter::onCreateNewMessageClicked

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.activity_message_list_container, fragment)
        transaction.commit()
    }
    /**
     * переход назад
     */
    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * переход к экрану деталки сообщения
     */
    override fun navigateToMessageDetail() {
        messageActivityNavigator.navigateToMessageDetailActivity(this)
    }
    /**
     * переход к написанию сообщения
     */
    override fun navigateToWriteMessage() {
        messageActivityNavigator.navigateToWriteMessageActivity(this)
    }

    fun getComponent(): MessageListComponent {
        return component
    }

    private fun getScreenComponent(): MessageListScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return MessagesComponent.get().messageListScreenComponent()
        } else {
            return saved as MessageListScreenComponent
        }

    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, MessageListActivity::class.java)
            return intent
        }

    }
}