package ru.digipeople.locotech.core.ui.activity.auth

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.core.data.CorePrefs
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.core.ui.helper.AuthInfoStorage
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewModel для активити авторищации
 */
class AuthViewModel @Inject constructor(
        private val userErrorBuilder: UserErrorBuilder,
        private val corePrefs: CorePrefs,
        private val navigator: CoreNavigator,
        private val authInfoStorage: AuthInfoStorage
) : BaseViewModel() {
    val errorLiveData = SingleEventLiveData<UserError>()
    val authSuccessLiveData = SingleEventLiveData<Unit>()
    val loginBtnAvailableLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userNameLiveData = MutableLiveData<String>()

    private val passwordFormat = { password: String -> password + "000000" }
    private var authDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        corePrefs.userName?.let {
            userNameLiveData.value = it
        }
    }
    /**
     * аворизация
     */
    fun onLoginBtnClicked(login: String, password: String) {
        if (!checkLoginAndPassword(login, password)) return
        val modifiedPassword = passwordFormat(password)
        authDisposable = AuthParams.authWorkerProxy!!.auth(login, modifiedPassword)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { loadingLiveData.value = true }
                .doOnError { loadingLiveData.value = false }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userError ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (userError == UserError.NO_ERROR) {
                        corePrefs.userName = login
                        authInfoStorage.loginPassPair = Pair(login, modifiedPassword)
                        authSuccessLiveData.value = Unit
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        errorLiveData.value = userError
                        loginBtnAvailableLiveData.value = true
                    }
                }, { throwable ->
                    errorLiveData.value = userErrorBuilder.fromThowable(throwable)
                    loginBtnAvailableLiveData.value = true
                })
    }
    /**
     * проверка логина и пароля
     */
    private fun checkLoginAndPassword(login: String, password: String): Boolean {
        if (!login.matches("^[a-zA-Zа-яА-Я0-9]*$".toRegex())) {
            errorLiveData.value = userErrorBuilder.userNameMatchError
            return false
        }
        /**
         * на символы
         */
        if (!password.matches("^[0-9]*$".toRegex())) {
            errorLiveData.value = userErrorBuilder.userPasswordMatchError
            return false
        }
        /**
         * на минимальную длину логина
         */
        if (login.length < MINIMAL_LOGIN_LENGTH) {
            errorLiveData.value = userErrorBuilder.userNameLengthError
            return false
        }
        /**
         * На длину пароля
         */
        if (password.length < MINIMAL_PASS_LENGTH) {
            errorLiveData.value = userErrorBuilder.userPasswordLengthError
            return false
        }

        return true
    }
    /**
     * переход к настройкам
     */
    fun onSettingsBtnClicked() {
        navigator.navigateSettings()
    }

    companion object {
        const val MINIMAL_LOGIN_LENGTH = 4
        const val MINIMAL_PASS_LENGTH = 4
    }
}
