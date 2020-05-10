package ru.digipeople.locotech.worker.ui.activity.task.interactor

import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import javax.inject.Inject

/**
 * Класс реализующий работу по запуску/остановке/завершения работы
 *
 * @author Kashonkov Nikita
 */
class WorkJob @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                  private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * начать работу
     */
    fun startWork(workId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.startWork(workId))
    }
    /**
     * Завершить работу
     */
    fun doneWork(workId: String): Single<Result>{
        return mapToResult(thingsWorxWorker.doneWork(workId))
    }
    /**
     * остановить работу
     */
    fun pauseWork(workId: String, reasonId: String): Single<Result>{
        return mapToResult(thingsWorxWorker.pauseWork(workId, reasonId))
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
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}