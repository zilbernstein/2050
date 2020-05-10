package ru.digipeople.message.ui.activity.messagedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import ru.digipeople.message.MessagesComponent
import ru.digipeople.message.R
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.message.ui.fragment.messagedetail.MessageDetailFragment
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import javax.inject.Inject

/**
 * Активити деталки сообщений
 *
 * @author Kashonkov Nikita
 */
class MessageDetailActivity : MvpActivity(), MessageDetailView {
    //region DI
    private lateinit var screenComponent: MessageDetailScreenComponent
    private lateinit var component: MessageDetailComponent
    @Inject
    lateinit var messageActivityNavigator: MessageActivityNavigator
    //endRegion
    //region View
    private lateinit var container: FrameLayout
    private lateinit var backImage: ImageView
    //endRegion
    //region Other
    private lateinit var presenter: MessageDetailPresenter
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, MessageDetailPresenter::class.java)
        presenter.initialize()

        container = findViewById(R.id.activity_message_detail_container)

        backImage = findViewById(R.id.back_button)
        /**
         * Обработка нажатия на кнопку назад
         */
        backImage.setOnClickListener { presenter.onBackButtonClicked() }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.activity_message_detail_container, MessageDetailFragment.newInstance())
        transaction.commit()
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

    fun getComponent(): MessageDetailComponent {
        return component
    }

    private fun getScreenComponent(): MessageDetailScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return MessagesComponent.get().messageDetailScreenComponent()
        } else {
            return saved as MessageDetailScreenComponent
        }

    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, MessageDetailActivity::class.java)
        }
    }
}