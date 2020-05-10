package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.api.model.response.MeasurementsTaskResponse
import ru.digipeople.locotech.master.model.MeasurementsTask
/**
 *  Маппер задач
 */
@Mapper(uses = [MeasurementStatusMapper::class])
interface MeasurementsTaskMapper {
    fun fromEntity(entity: MeasurementsTaskResponse?): MeasurementsTask?
}
        /*
        * Переменная для создания маппера
        */
        val measurementsTaskMapper = Mappers.getMapper(MeasurementsTaskMapper::class.java)!!