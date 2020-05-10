package ru.digipeople.locotech.core.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.R
import ru.digipeople.locotech.core.helper.Flavor
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.ui.fragment.dialog.LoadingDialog
import ru.digipeople.utils.input.Keyboard
import ru.digipeople.utils.model.UserError

/**
 * Активити авторизации
 *
 * @author Nikita Sychev
 **/
class AuthActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerAuthComponent.builder()
                .authModule(AuthModule())
                .coreAppComponent(CoreAppComponent.get())
                .build()
    }

    //endregion
    //region Views
    private lateinit var mainLayout: View
    private lateinit var inputUserName: EditText
    private lateinit var inputUserPass: EditText
    private lateinit var loginBtn: Button
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var specializationTitle: TextView
    private lateinit var logo: ImageView
    private lateinit var settingsButton: ImageView
    //endregion
    //region other
    private lateinit var viewModel: AuthViewModel
    //endregion
    /**
     * Действияпри старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = CoreAppComponent.get().screenOrientation()
        super.onCreate(savedInstanceState)
        component.inject(this)

        setContentView(R.layout.activity_auth_core)

        specializationTitle = findViewById(R.id.auth_activity_specialization_core)
        mainLayout = findViewById(R.id.auth_activity_main_layout_core)
        inputUserName = findViewById(R.id.auth_activity_login_core)
        inputUserPass = findViewById(R.id.auth_activity_password_core)
        loginBtn = findViewById(R.id.auth_activity_login_button_core)
        logo = findViewById(R.id.auth_activity_logo_core)
        settingsButton = findViewById(R.id.auth_activity_settings_image_button)

        specializationTitle.text = AuthParams.specializationTitleText

        if (Flavor.isDev) {
            inputUserName.setText(AuthParams.debugCredentials?.first ?: "")
            inputUserPass.setText(AuthParams.debugCredentials?.second ?: "")
        }
        /**
         * обработка нажатия на кнопку логина
         */
        loginBtn.setOnClickListener {
            onLoginBtnClicked()
        }
        /**
         * обработка ввода пароля
         */
        inputUserPass.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLoginBtnClicked()
                    return true
                }
                return false
            }
        })
        /**
         * Обработка нажатия кнопки настроек
         */
        settingsButton.setOnClickListener {
            viewModel.onSettingsBtnClicked()
        }

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(AuthViewModel::class.java)
        viewModel.also {
            it.errorLiveData.observe({ lifecycle }, ::showError)
            it.authSuccessLiveData.observe({ lifecycle }, { onAuthSuccess() })
            it.loadingLiveData.observe({ lifecycle }, ::setProgressVisibile)
            it.loginBtnAvailableLiveData.observe({ lifecycle }, ::setLoginBtnAvailable)
            it.userNameLiveData.observe({ lifecycle }, ::setUserName)

            it.start()
        }

        loadingDialog = LoadingDialog()
    }
    /**
     * нажатие на кнопку логина
     */
    private fun onLoginBtnClicked() {
        viewModel.onLoginBtnClicked(
                login = inputUserName.text.toString(),
                password = inputUserPass.text.toString()
        )
    }
    /**
     * Отображение ошибки
     */
    private fun showError(error: UserError) {
        Keyboard.hide(loginBtn)
        Snackbar.make(mainLayout, error.message, Snackbar.LENGTH_SHORT).show()
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setProgressVisibile(visible: Boolean) {
        if (visible) {
            loadingDialog.show(supportFragmentManager, PROGRESS_FRAGMNET_TAG)
        } else {
            loadingDialog.dismiss()
        }
    }
    /**
     * действия при успешной авторизации
     */
    private fun onAuthSuccess() {
        AuthParams.onSuccess?.invoke(this)
    }
    /**
     * управление доступностью кнопки логина
     */
    private fun setLoginBtnAvailable(available: Boolean) {
        loginBtn.isEnabled = available
    }
    /**
     * установка имени пользователя
     */
    private fun setUserName(userName: String) {
        inputUserName.setText(userName)
    }

    companion object {
        private const val PROGRESS_FRAGMNET_TAG = "PROGRESS_FRAGMNET_TAG"
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}