package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper
import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementEntity
import ru.digipeople.locotech.metrologist.data.model.Measurement
/**
 * Маппер замера
 *
 * @author Michael Solenov
 */
@Mapper(uses = [
    PerformerMapper::class,
    DateMapper::class
])
interface MeasurementMapper {
    fun fromEntity(entity: MeasurementEntity): Measurement
    fun fromEntityList(entities: List<MeasurementEntity>): List<Measurement>
    fun toEntity(entity: Measurement): MeasurementEntity
}
/**
 * Создание маппера
 */
val measurementMapper: MeasurementMapper = Mappers.getMapper(MeasurementMapper::class.java)