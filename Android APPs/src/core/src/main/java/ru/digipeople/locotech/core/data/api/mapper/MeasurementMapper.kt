package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.entity.MeasurementsEntity
import ru.digipeople.locotech.core.data.model.Measurement
import java.util.*

/**
 * Маппер для замеров
 *
 * @author Mike Solenov
 */
@Mapper(uses = [
    MeasureValueTypeMapper::class,
    CommentMapper::class,
    PerformerMapper::class,
    StageTypeMapper::class
])
abstract class MeasurementMapper : BaseMapper<MeasurementsEntity, Measurement>() {

    fun entityToModel(entity: Long): Date? {
        return if (entity == 0L) null else DateMapper.INSTANCE.entityToModel(entity)
    }

    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: MeasurementMapper = Mappers.getMapper(MeasurementMapper::class.java)
    }
}
