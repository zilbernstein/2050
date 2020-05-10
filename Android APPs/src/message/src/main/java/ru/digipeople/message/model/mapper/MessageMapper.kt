package ru.digipeople.message.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.message.api.model.MessageEntity
import ru.digipeople.message.model.Message

/**
 * Маппер для [MessageEntity]
 */
@Mapper(uses = [
    MessageStatusMapper::class,
    ContactMapper::class,
    DateMapper::class
])
interface MessageMapper {
    fun fromEntityList(entities: List<MessageEntity>): List<Message>
}
/**
 * Созданеи маппреа
 */
val messageMapper: MessageMapper = Mappers.getMapper(MessageMapper::class.java)