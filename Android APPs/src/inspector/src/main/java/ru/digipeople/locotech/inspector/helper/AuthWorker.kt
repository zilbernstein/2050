package ru.digipeople.locotech.inspector.helper

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.model.Client
import ru.digipeople.locotech.inspector.model.mapper.clientMapper
import ru.digipeople.locotech.inspector.model.mapper.userRoleMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс [AuthWorker] - реализует метод по авторизации пользователя
 *
 * @author Kashonkov Nikita
 */
class AuthWorker @Inject constructor(
        private val thingWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * Авторизация пользователя
     */
    fun auth(userName: String, password: String): Single<Result> {
        return thingWorxWorker.auth(userName, password)
                /**
                 * Обработка резульатата
                 */
                .map {
                    if (userRoleMapper.fromEntity(it.role) == null) {
                        Result(errorBuilder.wrongRole, null)
                    } else {
                        val client = clientMapper.fromEntity(it)
                        Result(UserError.NO_ERROR, client)
                    }
                }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }

    data class Result(val userError: UserError, val _client: Client?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
        val client
            get() = _client!!
    }
}