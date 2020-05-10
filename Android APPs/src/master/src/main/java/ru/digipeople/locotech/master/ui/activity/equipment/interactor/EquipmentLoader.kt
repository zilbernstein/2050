package ru.digipeople.locotech.master.ui.activity.equipment.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.Equipment
import ru.digipeople.locotech.master.model.mapper.EquipmentMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик списка оборудования, находящегося в ремонте
 *
 * @author Kashonkov Nikita
 */
class EquipmentLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * создание маппера
     */
    val mapper = EquipmentMapper.get()
    /**
     * загрузка оборудования и секций
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
     * стандартный ответ метода
     */
    class Result(val userError: UserError, val equipments: List<Equipment>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}