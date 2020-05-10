package ru.digipeople.locotech.inspector.ui.activity.comment.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс содержащий методы для работы с Work
 *
 * @author Kashonkov Nikita
 */
class CommentUploader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Добавить комментарий к работе
     */
    fun addWorkComment(workId: String, text: String): Single<Result> {
        return mapToResult(thingsWorxWorker.addWorkComent(workId, text))
    }
    /**
     * Добавить комментарий к замечанию
     */
    fun addRemarkComment(remarkId: String, text: String): Single<Result> {
        return mapToResult(thingsWorxWorker.addRemarkComent(remarkId, text))
    }
    /**
     * Добавить комментарий к ксо
     */
    fun addCsoComment(csoId: String, text: String): Single<Result> = mapToResult(thingsWorxWorker.editCsoComment(csoId, text))
    /**
     * преобразование данных
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }

    }
    /**
     * Стандартный ответ методов
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}