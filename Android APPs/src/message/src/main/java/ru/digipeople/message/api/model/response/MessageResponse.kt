package ru.digipeople.message.api.model.response

import ru.digipeople.message.api.model.MessageEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Ответ на метод получения списка сообщений
 *
 * @author Kashonkov Nikita
 */
class MessageResponse : BaseCollectionResponse<MessageEntity>()