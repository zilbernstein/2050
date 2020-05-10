package ru.digipeople.message.ui.activity.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.digipeople.message.R
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.message.ui.fragment.messagedetail.MessageDetailFragment
import ru.digipeople.message.ui.fragment.messagelist.MessageListFragment
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import javax.inject.Inject

/**
 * Активити сообщения
 *
 * @author Kashonkov Nikita
 */
class MessageActivity : MvpActivity(), MessageView {
    //region Di
    private lateinit var screenComponent: MessageScreenComponent
    private lateinit var component: MessageComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var messageActivityNavigator: MessageActivityNavigator
    //endregion
    //region Other
    private lateinit var presenter: MessagePresenter
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.messageComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.message_activity_title)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.messagePresenter() }, MessagePresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        /**
         * Фрагмент списка сообщений
         */
        var messageListFragment = supportFragmentManager.findFragmentById(R.id.message_list_container) as MessageListFragment?
        if (messageListFragment == null) {
            messageListFragment = MessageListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(R.id.message_list_container, messageListFragment, null)
                    .commit()
        }
        messageListFragment.onCreateMessageClickListener = presenter::onCreateMessageClicked

        /**
         * Фрагмент деталки сообщения
         */
        var messageDetailFragment = supportFragmentManager.findFragmentById(R.id.message_detail_container) as MessageDetailFragment?
        if (messageDetailFragment == null) {
            messageDetailFragment = MessageDetailFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(R.id.message_detail_container, messageDetailFragment, null)
                    .commit()
        }
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    fun getComponent(): MessageComponent {
        return component
    }
    /**
     * Переход к созданию нового сообщения
     */
    override fun navigateToNewMessage() {
        messageActivityNavigator.navigateToNewMessageActivity(this)
    }

    private fun getScreenComponent(): MessageScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return ru.digipeople.message.MessagesComponent.get().messageScreenComponent()
        } else {
            return saved as MessageScreenComponent
        }

    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, MessageActivity::class.java)
        }
    }

}