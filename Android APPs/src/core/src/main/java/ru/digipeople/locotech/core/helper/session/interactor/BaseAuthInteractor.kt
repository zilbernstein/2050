package ru.digipeople.locotech.core.helper.session.interactor

import ru.digipeople.locotech.core.data.api.CoreThingsWorxWorker
import ru.digipeople.locotech.core.data.api.response.SignInResponse
import ru.digipeople.locotech.core.push.service.FbcTokenManager
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError

/**
 * Базовый класс для авторизации
 */
abstract class BaseAuthInteractor<T> constructor(private val thingsWorxWorker: CoreThingsWorxWorker,
                                                 private val errorBuilder: SimpleApiUserErrorBuilder,
                                                 private val fbcTokenManager: FbcTokenManager) {

    abstract fun mapAuthResponseToAuthInfo(signInResponse: SignInResponse): T
    /*
         * авторизацияя
         */
    suspend fun auth(userName: String, password: String): Result<T> {
        return try{
        /*
        * подключение
        */
            val connected = thingsWorxWorker.connect(userName, password)
            if (!connected) {
                return Result(errorBuilder.wrongUser, null)
            }
            val authResponse = thingsWorxWorker.signIn(fbcTokenManager.token)
            val authInfo = mapAuthResponseToAuthInfo(authResponse)
            Result(UserError.NO_ERROR, authInfo)
        } catch (e: Exception) {
            val userError = errorBuilder.fromThowable(e)
            Result(userError, null)
        }
    }
    /**
     * Стандартный ответ метода
     */
    class Result<T>(val userError: UserError, private val _authInfo: T?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
        val authInfo
            get() = _authInfo!!
    }
}