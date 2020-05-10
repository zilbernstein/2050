package ru.digipeople.locotech.master.ui.activity.ordertorepair

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Активити запрос на получение ТМЦ для ремонта
 */
class OrderToRepairActivity : AppActivity() {

    //region Di
    private val component by lazy {
        DaggerOrderToRepairComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .coreAppComponent(CoreAppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    //endregion
    //region Views
    private lateinit var statusTextView: TextView
    private lateinit var sendButton: Button
    //endregion
    //region Other
    private lateinit var viewModel: OrderToRepairViewModel
    //endregion
    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_order_to_repair)

        statusTextView = findViewById(R.id.order_status)
        statusTextView.visibility = View.INVISIBLE
        sendButton = findViewById(R.id.send_order_to_repair)
        sendButton.setOnClickListener { viewModel.onSendButtonClicked() }
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.order_to_repair_title)
        /**
         * инициализация меню
         */
        mainDrawerDelegate.init(iconBack = false)
        /**
         * инициализация viewModel
         */
        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(OrderToRepairViewModel::class.java)
        viewModel.apply {
            loadingLiveData.observe({ lifecycle }, ::setLoadingVisible)
            userErrorLiveData.observe({ lifecycle }, ::showUserError)
            statusLiveData.observe({ lifecycle }, ::showStatus)

            start()
        }
    }
    /**
     * отображение статуса
     */
    private fun showStatus(status: String) {
        statusTextView.visibility = View.VISIBLE
        sendButton.visibility = View.INVISIBLE
        statusTextView.text = status
    }
    /**
     * управление видимостью лоадера
     */
    private fun setLoadingVisible(visible: Boolean) = loadingFragmentDelegate.setLoadingVisibility(visible)
    /**
     * отображение ошибки пользователя
     */
    private fun showUserError(userError: UserError) {
        Snackbar.make(statusTextView, userError.message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * интент
         */
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, OrderToRepairActivity::class.java)
        }
    }
}