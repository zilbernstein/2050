package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.worker.api.model.response.MeasurementsTaskResponse
import ru.digipeople.locotech.worker.model.MeasurementsTask

/**
 * Маппер для запроса задания на проведение аппаратных замеров
 */
@Mapper(uses = [MeasurementStatusMapper::class])
interface MeasurementsTaskMapper {
    fun fromEntity(entity: MeasurementsTaskResponse?): MeasurementsTask?
}
        /*
        * Создание маппера
        */
val measurementsTaskMapper = Mappers.getMapper(MeasurementsTaskMapper::class.java)!!