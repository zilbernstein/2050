package ru.digipeople.message.ui.fragment.messagedetail.interactor

import io.reactivex.Single
import ru.digipeople.message.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Отмечает сообщение прочитанным
 */
class MessageReader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * отметить как прочитанное
     */
    fun markAsRead(id: String): Single<Result> {
        return thingsWorxWorker.markMessageAsRread(id)
                .map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
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