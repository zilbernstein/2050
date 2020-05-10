package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс для выбора причины обточки
 *
 * @author Michael Solenov
 */
class SetReasonInteractor @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                              private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Выбор причины
     */
    fun setProcessingReason(reasonId: String, wheelPairId: String, measurementId: String): Single<Result> {
        return thingsWorxWorker.setProcessingReason(reasonId, wheelPairId, measurementId)
                .andThen(Single.just(Result(UserError.NO_ERROR)))
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