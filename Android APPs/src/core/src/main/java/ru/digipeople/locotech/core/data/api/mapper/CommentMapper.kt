package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.entity.CommentEntity
import ru.digipeople.locotech.core.data.model.Comment

/**
 * Маппер для комментария
 */
@Mapper(uses = [DateMapper::class])
abstract class CommentMapper : BaseMapper<CommentEntity, Comment>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: CommentMapper = Mappers.getMapper(CommentMapper::class.java)
    }
}