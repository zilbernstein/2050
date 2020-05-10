package ru.digipeople.message.ui.activity.writemessage.interactor

import io.reactivex.Single
import ru.digipeople.message.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Отправляет сообщение списку адресатов
 */
class MessageSender @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Отправка сообщения
     */
    fun sendMessage(contacts: Collection<String>, message: String): Single<Result> {
        return thingsWorxWorker.sendMessage(contacts, message)
                .map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}