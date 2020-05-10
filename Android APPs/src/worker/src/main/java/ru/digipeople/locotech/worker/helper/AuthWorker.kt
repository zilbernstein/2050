package ru.digipeople.locotech.worker.helper

import io.reactivex.Single
import ru.digipeople.locotech.core.push.service.FbcTokenManager
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.Client
import ru.digipeople.locotech.worker.model.mapper.ClientMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс [AuthWorker] - реализует метод по авторизации пользователя
 *
 * @author Kashonkov Nikita
 */
class AuthWorker @Inject constructor(private val thingWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder,
                                     private val fbcTokenManager: FbcTokenManager) {
    /**
     * Созждание маппера
     */
    val mapper = ClientMapper.INSTANCE
    /**
     * Авторизация
     */
    fun auth(userName: String, password: String): Single<Result> {
        return thingWorxWorker.auth(userName, password, fbcTokenManager.token)
                .map { mapper.entityToModel(it)!! }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val client: Client?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}