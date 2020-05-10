package ru.digipeople.locotech.master.ui.activity.groupassignment.iteractor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.WorkAndBrigadeForAssignment
import ru.digipeople.locotech.master.model.mapper.WorkAndBrigadesMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик группового назначения исполнителей
 *
 * @author Sostavkin Grisha
 */
class WorkAndBrigadesLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                                private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = WorkAndBrigadesMapper.INSTANCE
    /**
     * Загрузка списка исполнителей и бригад
     */
    fun loadWorkAndBrigade(): Single<Result> {
        return thingsWorxWorker.getWorkAndBrigadesForAssignment()
                .map {response ->
                    /**
                     * обработка результата
                     */
                    Result(UserError.NO_ERROR, mapper.entityToModel(response))
                }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val workAndBrigadeList: WorkAndBrigadeForAssignment?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}