package ru.digipeople.locotech.worker.ui.activity.task.interactor

import io.reactivex.Single
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.WorkDetail
import ru.digipeople.locotech.worker.model.mapper.workDetailMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик деталей работы
 */
class WorkLoader @Inject constructor(private val thingWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузщка списка работ
     */
    fun loadWork(workId: String): Single<Result> {
        return thingWorxWorker.getWorkDetail(workId)
                .map { workDetailMapper.fromEntity(it)!! }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError,
                 private val _workDetail: WorkDetail?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
        val workDetail: WorkDetail
            get() = _workDetail!!
    }

}