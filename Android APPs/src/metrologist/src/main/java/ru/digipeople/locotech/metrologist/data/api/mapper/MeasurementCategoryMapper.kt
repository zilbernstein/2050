package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementCategoryEntity
import ru.digipeople.locotech.metrologist.data.model.MeasurementCategory
/**
 * Маппер категорий замеров
 */
@Mapper
interface MeasurementCategoryMapper {
    fun fromEntity(entity: MeasurementCategoryEntity): MeasurementCategory
    fun fromEntityList(entities: List<MeasurementCategoryEntity>): List<MeasurementCategory>
    fun toEntity(entity: MeasurementCategory): MeasurementCategoryEntity
}
/**
 * Создание маппера
 */
val measurementCategoryMapper: MeasurementCategoryMapper = Mappers.getMapper(MeasurementCategoryMapper::class.java)