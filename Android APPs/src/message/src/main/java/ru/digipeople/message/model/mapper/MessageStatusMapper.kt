package ru.digipeople.message.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.message.model.MessageStatus

/**
 * Маппер для статуса сообщения
 */
@Mapper
abstract class MessageStatusMapper {
    /**
     * формирование статуса
     */
    fun fromEntity(entity: String?) = when (entity) {
        "Отправлено" -> MessageStatus.SENT
        "отправлено" -> MessageStatus.SENT
        "Прочитано" -> MessageStatus.READ
        "прочитано" -> MessageStatus.READ
        else -> MessageStatus.UNKNOWN
    }
}