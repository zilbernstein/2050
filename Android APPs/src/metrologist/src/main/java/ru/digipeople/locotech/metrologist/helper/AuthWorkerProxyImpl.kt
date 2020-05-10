package ru.digipeople.locotech.metrologist.helper

import io.reactivex.Single
import ru.digipeople.locotech.core.ui.activity.auth.interactor.AuthWorkerProxy
import ru.digipeople.locotech.metrologist.helper.session.SessionManager
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * обертка для метода авторизации
 *
 * @author Kashonkov Nikita
 */
class AuthWorkerProxyImpl @Inject constructor(private val sessionManager: SessionManager) : AuthWorkerProxy {
    override fun auth(login: String, password: String): Single<UserError> {
        /**
         * Начать сессию
         */
        return sessionManager.startSession(login, password)
    }
}