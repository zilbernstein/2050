package ru.digipeople.thingworx.helper

import android.content.Context
import com.google.gson.JsonSyntaxException
import ru.digipeople.thingworx.R
import ru.digipeople.utils.model.UserError
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * Билдер [UserError]
 *
 * @author Kashonkov Nikita
 */
open class SimpleApiUserErrorBuilder @Inject constructor(protected open val context: Context) {
    /**
     * подключение логгирования
     */
    private val logger = ru.digipeople.logger.LoggerFactory.getLogger(SimpleApiUserErrorBuilder::class)
    /**
     * перечень ошибок
     * неизвестная ошибка
     */
    protected open val unknownErrorMsg: String
        get() = context.getString(R.string.simple_api_error_unknown)
    /**
     * нет ответа от сервера
     */
    protected open val timeoutErrorMsg: String
        get() = context.getString(R.string.timeout_error)
    /**
     * некорректные данные от сервера
     */
    protected open val wrongDataErrorMsg: String
        get() = context.getString(R.string.wrong_data_error)
    /**
     * у пользователя нет доступа в приложение
     */
    protected open val wrongRoleUserMsg: String
        get() = context.getString(R.string.wrong_role_user)
    /**
     * проверьте логиню пароль и подключение к серверу
     */
    protected open val wrongUserPassword: String
        get() = context.getString(R.string.wrong_user_password)

    open val unknownError: UserError
        get() = Error(unknownErrorMsg)

    open val wrongRole: UserError
        get() = Error(wrongRoleUserMsg)

    open val timeoutError: UserError
        get() = Error(timeoutErrorMsg)

    open val wrongDataError: UserError
        get() = Error(wrongDataErrorMsg)

    open val wrongUser: UserError
        get() = Error(wrongUserPassword)

    open fun fromThowable(throwable: Throwable): UserError {
        logger.error("fromThrowable", throwable)
        return when (throwable) {
            is ApiException -> {
                Error(context.getString(R.string.simple_api_error, throwable.message!!))
            }
            is TimeoutException -> timeoutError
            is NullPointerException -> wrongDataError
            is JsonSyntaxException -> wrongDataError
            is AuthorizationMistakeException -> wrongUser
            else -> unknownError
        }
    }
    /**
     * Класс ошибки
     */
    private class Error(override val message: String) : UserError
}