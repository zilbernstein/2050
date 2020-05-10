package ru.digipeople.locotech.master.ui.activity.urgent.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.mapper.WorkMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик срочно
 *
 * @author Kashonkov Nikita
 */
class UrgentLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = WorkMapper.INSTANCE
    /**
     * Загрузка явки
     */
    fun loadUrgent(): Single<Result>{
        return thingsWorxWorker.getUrgent()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    /**
                     * обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val works: List<Work>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}