package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementInfoEntity
import ru.digipeople.locotech.metrologist.data.model.MeasurementInfo

/**
 * Маппер информации по замеру
 *
 * @author Michael Solenov
 */
@Mapper(uses = [
    MeasurementMapper::class,
    ParameterMapper::class,
    DateMapper::class
])
interface MeasurementInfoMapper {
    fun fromEntity(entity: MeasurementInfoEntity): MeasurementInfo
    fun toEntity(entity: MeasurementInfo): MeasurementInfoEntity
}
/**
 * Создание маппера
 */
val measurementInfoMapper: MeasurementInfoMapper = Mappers.getMapper(MeasurementInfoMapper::class.java)