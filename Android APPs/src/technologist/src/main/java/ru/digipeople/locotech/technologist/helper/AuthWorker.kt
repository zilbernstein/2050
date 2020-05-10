package ru.digipeople.locotech.technologist.helper

import io.reactivex.Single
import ru.digipeople.locotech.core.push.service.FbcTokenManager
import ru.digipeople.locotech.technologist.api.ThingsWorxWorker
import ru.digipeople.locotech.technologist.mapper.ClientMapper
import ru.digipeople.locotech.technologist.model.Client
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс [AuthWorker] - реализует метод по авторизации пользователя
 *
 * @author Sostavkin Grisha
 */
class AuthWorker @Inject constructor(private val thingWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder,
                                     private val fbcTokenManager: FbcTokenManager) {
    /**
     * Создание маппера
     */
    val mapper = ClientMapper.INSTANCE
    /**
     * авторизация
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