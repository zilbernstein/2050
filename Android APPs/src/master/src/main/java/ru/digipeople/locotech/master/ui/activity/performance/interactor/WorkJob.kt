package ru.digipeople.locotech.master.ui.activity.performance.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс содержащий методы для работы с Work
 *
 * @author Kashonkov Nikita
 */
class WorkJob @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                  private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * остановить работу
     */
    fun stopWork(workId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.stopWork(workId))
    }
    /**
     * принять работу
     */
    fun acceptWork(workId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.acceptWork(workId))
    }
    /**
     * начать работу
     */
    fun startWork(workId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.startWork(workId))
    }
    /**
     * преобразование данных к требуемомоу виду
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }

    }
    /**
     * стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}