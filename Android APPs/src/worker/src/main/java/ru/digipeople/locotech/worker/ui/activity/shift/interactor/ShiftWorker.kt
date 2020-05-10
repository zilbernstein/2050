package ru.digipeople.locotech.worker.ui.activity.shift.interactor

import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import javax.inject.Inject

/**
 * Класс отправлющий запрос на старт/остановку смены
 *
 * @author Sostavkin Grisha
 */
class ShiftWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Старт смены
     */
    fun startShift(): Single<Result> {
        return mapToResult(thingsWorxWorker.startShift())
    }
    /**
     * Конец смены
     */
    fun stopShift(): Single<Result> {
        return mapToResult(thingsWorxWorker.stopShift())
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