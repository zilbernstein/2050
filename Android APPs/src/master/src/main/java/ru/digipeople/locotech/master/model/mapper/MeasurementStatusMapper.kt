package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.model.MeasurementStatus
/**
 *  Маппер состояния
 */
@Mapper
abstract class MeasurementStatusMapper {
    /*
    * Статусы задания на проведение замера
    */
    fun fromEntity(entity: String?) = when (entity) {
        "no_task" -> MeasurementStatus.NO_TASK
        "waiting" -> MeasurementStatus.WAITING
        "received" -> MeasurementStatus.RECEIVED
        else -> null
    }
}
/*
* Переменная для создания маппера
*/
val measurementStatusMapper: MeasurementStatusMapper = Mappers.getMapper(MeasurementStatusMapper::class.java)