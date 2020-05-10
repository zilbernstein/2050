package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс - очиста выбора причины обточки
 *
 * @author Michael Solenov
 */
class ClearTurningReasonInteractor @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Оочистка выбора прияины
     */
    fun clearReason(wheelPairId: String, measurementId: String): Single<Result> {
        return thingsWorxWorker.setProcessingReason("", wheelPairId, measurementId)
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