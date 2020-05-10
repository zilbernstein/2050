package ru.digipeople.locotech.inspector.ui.activity.inspection.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющий метод подтверждения замечаний РЖД
 * @author Kashonkov Nikita
 */
class InspectionWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                           private val errorBuilder: SimpleApiUserErrorBuilder) {


    /**
     * подтверждение РЖД
     */
    fun notifyRzd() = mapToResult(thingsWorxWorker.notifyRzd())
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
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}