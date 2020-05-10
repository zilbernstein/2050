package ru.digipeople.telephonebook.api.response

import ru.digipeople.telephonebook.api.model.ContactEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Обертка для модели списка контактов
 *
 * @author Sostavkin Grisha
 */
class ContactListResponse : BaseCollectionResponse<ContactEntity>()