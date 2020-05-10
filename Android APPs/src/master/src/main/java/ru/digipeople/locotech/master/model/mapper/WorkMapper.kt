package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.Work
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.api.model.WorkEntity
import ru.digipeople.locotech.core.data.api.mapper.CommentMapper
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper

/**
 * Маппер работы
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [WorkStatusMapper::class, PerformerMapper::class, CommentMapper::class])
abstract class WorkMapper: BaseMapper<WorkEntity, Work>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkMapper = Mappers.getMapper(WorkMapper::class.java)
    }
}