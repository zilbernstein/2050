package ru.digipeople.message.api.model.response

import ru.digipeople.message.api.model.ContactEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Ответ на метод получения списка контактов
 *
 * @author Kashonkov Nikita
 */
class ContactResponse: BaseCollectionResponse<ContactEntity>()