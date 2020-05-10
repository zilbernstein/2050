package ru.digipeople.locotech.technologist.ui.activity.comment.interactor

import io.reactivex.Single
import ru.digipeople.locotech.technologist.api.ThingsWorxWorker
import ru.digipeople.locotech.technologist.mapper.InteractionResultMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Редактирование коментария
 *
 * @author Sostavkin Grisha
 */
class CommentWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = InteractionResultMapper.INSTANCE
    /**
     * редактирование комментария
     */
    fun editComment(idWork: String, text: String): Single<Result> {
        return mapToResult(thingsWorxWorker.setWorkComment(idWork,text))
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