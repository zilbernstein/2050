package ru.digipeople.locotech.master.ui.activity.groupassignment.iteractor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.mapper.InteractionResultMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс - устанавливает выбранных исполнителей на выбранные задачи
 *
 * @author Sostavkin Grisha
 */
class WorkAndBrigadesWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                                private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * создание маппера
     */
    val mapper = InteractionResultMapper.INSTANCE
    /**
     * назначение исполнителей на работы
     */
    fun assignWorkersGroupForWorks(workList: List<String>, workerList: List<String>): Single<Result> {
        return thingsWorxWorker.assignGroupWorkersToWorks(workList, workerList).map {
            /**
             * обработка результата
             */
            Result(UserError.NO_ERROR)
        }.onErrorReturn {
            val userError = errorBuilder.fromThowable(it)
            Result(userError)
        }
    }
    /**
     * стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}