package ru.digipeople.locotech.master.ui.activity.comment.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.api.model.response.IndicatorResponse
import ru.digipeople.locotech.master.model.Indicator
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс содержащий методы для работы с комментариями
 *
 * @author Kashonkov Nikita
 */
class CommentUploader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * добавление комментария к работе
     */
    fun addWorkComment(workId: String, text: String): Single<Result> {
        return mapToResult(thingsWorxWorker.addWorkComent(workId, text))
    }
    /**
     * добавление комментария к замечанию
     */
    fun addRemarkComment(remarkId: String, text: String): Single<Result> {
        return mapToResult(thingsWorxWorker.addRemarkComent(remarkId, text))
    }
    /**
     * комментарий при редактировании замера
     */
    fun editMeasure(workId: String, measureList: Indicator): Single<Result> {
        return mapToResult(thingsWorxWorker.editMeasure(workId, measureToModel(measureList)))
    }
    /**
     * преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * преобразование замера к моделе
     */
    private fun measureToModel(it: Indicator): IndicatorResponse {
        val indicatorResponse = IndicatorResponse(it.measurementId, it.characteristicId, it.measureStage, it.measureValue, it.comment)
        return indicatorResponse
    }
    /**
     * стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}