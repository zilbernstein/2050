package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.SignerItemEntity
import ru.digipeople.locotech.inspector.model.SignerItem

/**
 * @author Kashonkov Nikita
 */
@Mapper(uses = [SignerMapper::class])
abstract class SignerItemMapper: BaseMapper<SignerItemEntity, SignerItem>() {
    companion object {
        /**
         * Переменная для создания маппера
         */
        val INSTANCE: SignerItemMapper = Mappers.getMapper(SignerItemMapper::class.java)
    }
}