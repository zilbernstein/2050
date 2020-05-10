package ru.digipeople.telephonebook.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.telephonebook.api.model.ContactEntity
import ru.digipeople.telephonebook.mapper.base.BaseMapper
import ru.digipeople.telephonebook.model.Contact

/**
 * Маппер для контакта
 *
 * @author Sostavkin Grisha
 */
@Mapper
abstract class ContactMapper : BaseMapper<ContactEntity, Contact>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: ContactMapper = Mappers.getMapper(ContactMapper::class.java)
    }
}