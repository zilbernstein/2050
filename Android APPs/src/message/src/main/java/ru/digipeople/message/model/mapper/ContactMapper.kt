package ru.digipeople.message.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.message.api.model.ContactEntity
import ru.digipeople.message.model.Contact

/**
 * Маппер для [ContactEntity]
 */
@Mapper
interface ContactMapper {
    fun fromEntityList(entities: List<ContactEntity>): List<Contact>
}
/**
 * Созданеи маппера
 */
val contactMapper: ContactMapper = Mappers.getMapper(ContactMapper::class.java)