package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.CommentMapper
import ru.digipeople.locotech.inspector.api.model.WorkEntity
import ru.digipeople.locotech.inspector.model.Work
/**
 * Маппер для работы
 */
@Mapper(uses = [WorkStatusMapper::class, CommentMapper::class])
abstract class WorkMapper : BaseMapper<WorkEntity, Work>() {
    companion object {
        /**
         * Переменная для создания маппера
         */
        val INSTANCE: WorkMapper = Mappers.getMapper(WorkMapper::class.java)
    }
}