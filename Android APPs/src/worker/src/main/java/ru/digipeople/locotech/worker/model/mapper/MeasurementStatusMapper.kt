package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.worker.model.MeasurementStatus

/**
 * Маппер для статуса замеров
 */
@Mapper
abstract class MeasurementStatusMapper {
    fun fromEntity(entity: String?) = when (entity) {
        "no_task" -> MeasurementStatus.NO_TASK
        "waiting" -> MeasurementStatus.WAITING
        "received" -> MeasurementStatus.RECEIVED
        else -> null
    }
}

        /*
        * Создание маппера
        */
val measurementStatusMapper: MeasurementStatusMapper = Mappers.getMapper(MeasurementStatusMapper::class.java)