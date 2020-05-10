package ru.digipeople.locotech.worker.ui.activity.choosereason.interactor

import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.PauseReason
import ru.digipeople.locotech.worker.model.mapper.ReasonMapper
import javax.inject.Inject

/**
 * Загрузка списка причин приостановки работ
 *
 * @author Kashonkov Nikita
 */
class ReasonLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Созадние маппера
     */
    val mapper =ReasonMapper.INSTANCE
    /**
     * Загрузка причин остановки работы
     */
    fun loadReasons(): Single<Result> {
        return thingsWorxWorker.getReasons()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError,
                      val reasons: List<PauseReason>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}