package ru.digipeople.locotech.master.helper

import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import ru.digipeople.locotech.core.ui.activity.auth.interactor.AuthWorkerProxy
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Передает [ClientProvider] текущего пользователя
 *
 * @author Nikita Sychev
 **/
class AuthWorkerProxyImpl @Inject constructor(
        private val sessionManager: SessionManager
) : AuthWorkerProxy {
//    override fun auth(login: String, password: String): Single<UserError> {
//
//        rxSingle {
//            sessionManager.startSession(login,password)
//        }
//        return authWorker.auth(login, password)
//                .doOnSuccess { result ->
//                    if (result.isSuccessful) {
//                        clientProvider.client = result.client!!
//                        equipmentProvider.equipment = result.client.equipment
//                    }
//                }
//                .map { it.userError }
//    }

    /**
     * переопределение функции аутентификации
     */
    override fun auth(login: String, password: String): Single<UserError> = rxSingle {
        sessionManager.startSession(login, password)
    }
}