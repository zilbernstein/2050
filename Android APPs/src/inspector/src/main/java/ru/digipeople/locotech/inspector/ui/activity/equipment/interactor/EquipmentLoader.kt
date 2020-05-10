package ru.digipeople.locotech.inspector.ui.activity.equipment.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.model.Equipment
import ru.digipeople.locotech.inspector.model.mapper.EquipmentMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик локомотивов
 */
class EquipmentLoader @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * создание маппера
     */
    val mapper = EquipmentMapper.get()
    /**
     * загрузка оборудования
     */
    fun loadEquipment(): Single<Result> {
        return thingsWorxWorker.getEquipment()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val equipments: List<Equipment>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}