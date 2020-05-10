package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.entity.ProcessingReasonEntity
import ru.digipeople.locotech.metrologist.data.model.Reason
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик выбора причины обточки
 *
 * @author Michael Solenov
 */
class TuningReasonsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                              private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка данных
     */
    fun load(): Single<Result> {
        return thingsWorxWorker.getProcessingReasons()
                .map { mapToResult(it.entityList) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Обработка результата
     */
    private fun mapToResult(reasonEntities: List<ProcessingReasonEntity>): Result {
        val reasons = reasonEntities.map {
            Reason().apply {
                id = it.id
                name = it.name
            }
        }
        return Result(UserError.NO_ERROR, reasons)
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError,
                 val reasons: List<Reason>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}