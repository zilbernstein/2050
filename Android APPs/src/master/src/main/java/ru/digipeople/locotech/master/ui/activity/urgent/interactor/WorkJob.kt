package ru.digipeople.locotech.master.ui.activity.urgent.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * класс работы срочно
 *
 * @author Kashonkov Nikita
 */
class WorkJob @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                  private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Остановить работу
     */
    fun stopWork(workId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.stopWork(workId))
    }
    /**
     * Принять работу
     */
    fun acceptWork(workId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.acceptWork(workId))
    }
    /**
     * Начать работу
     */
    fun startWork(workId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.startWork(workId))
    }
    /**
     * Преобразовние данных для требуемого вида
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