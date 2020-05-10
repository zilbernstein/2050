package ru.digipeople.locotech.worker.ui.activity.shift

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.Client
import ru.digipeople.locotech.worker.ui.activity.ActivityNavigator
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import javax.inject.Inject

/**
 * Активити смены
 *
 * @author Kashonkov Nikita
 */
class ShiftActivity : MvpActivity(), ShiftView {
    //region DI
    private lateinit var screenComponent: ShiftScreenComponent
    lateinit var component: ShiftComponent
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var navigator: Navigator
    //endRegion
    //regionView
    private lateinit var statusText: TextView
    private lateinit var welcomeText: TextView
    private lateinit var workButton: Button
    private lateinit var parentLayout: androidx.drawerlayout.widget.DrawerLayout
    //endRegion
    //region Other
    private lateinit var presenter: ShiftPresenter
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.shift_activity_title)
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        statusText = findViewById(R.id.activity_shift_status)
        welcomeText = findViewById(R.id.activity_shift_welcome)
        workButton = findViewById(R.id.activity_shift_work_button)
        parentLayout = findViewById(R.id.drawerLayout)
        workButton.setOnClickListener { presenter.onWorkButtonClicked() }

        presenter = getMvpDelegate().getPresenter(component::presenter, ShiftPresenter::class.java)
    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действи япри возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при приостановке активити
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    override fun setUpView(client: Client, isInWork: Boolean) {
        welcomeText.text = getString(R.string.shift_activity_welcome, client.lastName, client.name)
        showWorkStatus(isInWork)
    }
    /**
     * Отображение статуса и наименования кнопки
     */
    override fun showWorkStatus(isInWork: Boolean) {
        if (isInWork) {
            statusText.setText(R.string.shift_activity_status_work)
            statusText.setTextColor(ContextCompat.getColor(this, R.color.appGreen))
            workButton.setText(R.string.shift_activity_status_end_work)
            workButton.background = ContextCompat.getDrawable(this, R.drawable.blue_btn)
        } else {
            statusText.setText(R.string.shift_activity_status_free)
            statusText.setTextColor(ContextCompat.getColor(this, R.color.appRed))
            workButton.setText(R.string.shift_activity_status_start_work)
            workButton.background = ContextCompat.getDrawable(this, R.drawable.green_btn)
        }
    }
    /**
     * Управление видимостью кнопки
     */
    override fun setLoading(isLoading: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isLoading)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): ShiftScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().shiftScreenComponent()
        } else {
            return saved as ShiftScreenComponent
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            /**
             * Интент
             */
            return Intent(context, ShiftActivity::class.java)
        }

    }
}
