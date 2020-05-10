package ru.digipeople.locotech.worker.ui.activity.mytask.interactor

import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.Equipment
import ru.digipeople.locotech.worker.model.mapper.EquipmentMapper
import javax.inject.Inject

/**
 * Загрузка списка работ
 *
 * @author Kashonkov Nikita
 */
class WorkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = EquipmentMapper.INSTANCE
    /**
     * Загрузка данных
     */
    fun loadEquipments(): Single<Result> {
        return thingsWorxWorker.getWorks()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError,
                      val equipments: List<Equipment>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}