package ru.digipeople.locotech.master.ui.activity.partlyaccept.intercator

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющий частичную приемку работы
 *
 * @author Kashonkov Nikita
 */

class PartlyAcceptWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * частичная приемка работы
     */
    fun partlyAcceptWork(workId: String, dividedTime: Long): Single<Result> {
        return mapToResult(thingsWorxWorker.partlyAcceptWork(workId, dividedTime))
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
     * пример ответа метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}