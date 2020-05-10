package ru.digipeople.locotech.metrologist.helper.session.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.model.AuthInfo
import ru.digipeople.locotech.metrologist.data.api.mapper.AuthInfoMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Выполняет метод аутентификации текущего пользователя
 */
class AuthInteractor @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                         private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Созданеи маппера
     */
    val mapper = AuthInfoMapper.INSTANCE
    /**
     * авторизация
     */
    fun auth(userName: String, password: String): Single<Result> {
        return thingsWorxWorker.auth(userName, password)
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
    class Result(val userError: UserError, val authInfo: AuthInfo?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}