package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.RemarkEntity
import ru.digipeople.locotech.master.model.Remark
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.CommentMapper
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper

/**
 * Маппер замечания
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [WorkMapper::class, RemarkStatusMapper::class, CommentMapper::class, PerformerMapper::class])
abstract class RemarkMapper: BaseMapper<RemarkEntity, Remark>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: RemarkMapper = Mappers.getMapper(RemarkMapper::class.java)
    }

}