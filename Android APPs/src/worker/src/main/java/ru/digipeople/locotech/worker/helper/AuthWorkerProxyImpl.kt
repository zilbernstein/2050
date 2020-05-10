package ru.digipeople.locotech.worker.helper

import io.reactivex.Single
import ru.digipeople.locotech.core.ui.activity.auth.interactor.AuthWorkerProxy
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Передает [ClientProvider] текущего пользователя
 */
class AuthWorkerProxyImpl @Inject constructor(private val authWorker: AuthWorker,
                                              private val clientProvider: ClientProvider) : AuthWorkerProxy {
    override fun auth(login: String, password: String): Single<UserError> {
        return authWorker.auth(login, password)
                .doOnSuccess { result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        clientProvider.client = result.client!!
                    }
                }
                .map { it.userError }
    }
}