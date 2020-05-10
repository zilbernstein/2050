package ru.digipeople.locotech.inspector.helper

import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import ru.digipeople.locotech.core.ui.activity.auth.interactor.AuthWorkerProxy
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.helper.ClientProvider
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Передает [SessionManager] текущего пользователя
 */

class AuthWorkerProxyImpl @Inject constructor(
        private val sessionManager: SessionManager
) : AuthWorkerProxy {
    /**
     * Авторизация
     */
    override fun auth(login: String, password: String): Single<UserError> = rxSingle {
        sessionManager.startSession(login, password)
    }
}