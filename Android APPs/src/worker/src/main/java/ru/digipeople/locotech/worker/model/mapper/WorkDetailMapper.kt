package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.CommentMapper
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.worker.api.model.WorkDetailEntity
import ru.digipeople.locotech.worker.model.WorkDetail

/**
 * Маппер для деталки работы
 */
@Mapper(uses = [
    DateMapper::class,
    WorkStatusMapper::class,
    CommentMapper::class,
    TMCInWorkMapper::class,
    MeasurementStatusMapper::class
])
interface WorkDetailMapper {
    fun fromEntity(entity: WorkDetailEntity?): WorkDetail?
}
/*
        * Создание маппера
        */
val workDetailMapper: WorkDetailMapper = Mappers.getMapper(WorkDetailMapper::class.java)