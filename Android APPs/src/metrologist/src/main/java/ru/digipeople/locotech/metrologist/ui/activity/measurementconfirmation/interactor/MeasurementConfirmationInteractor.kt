package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Метод выполняющий метод post на экране замечания
 *
 * @author Michael Solenov
 */
class MeasurementConfirmationInteractor @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Отправить данных замера
     */
    fun post(measurementId: String): Single<Result> {
        return thingsWorxWorker.postCompleteMeasurement(measurementId)
                .andThen(Single.just(Result(UserError.NO_ERROR)))
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