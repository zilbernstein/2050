package ru.digipeople.locotech.worker.ui.activity.comment.interactor

import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import javax.inject.Inject

/**
 * Класс содержащий методы для работы с Work
 *
 * @author Kashonkov Nikita
 */
class CommentUploader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Добавить комментарий
     */
    fun addWorkComment(workId: String, text: String): Single<Result> {
        return mapToResult(thingsWorxWorker.addWorkComment(workId, text))
    }
    /**
     * Преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}