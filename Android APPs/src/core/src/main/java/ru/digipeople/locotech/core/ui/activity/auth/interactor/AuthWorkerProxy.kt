package ru.digipeople.locotech.core.ui.activity.auth.interactor

import io.reactivex.Single
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс метода авторизации
 *
 * @author Nikita Sychev
 **/
interface AuthWorkerProxy {
    fun auth(login: String, password: String): Single<UserError>
}