package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.SignerEntity
import ru.digipeople.locotech.inspector.model.Signer

/**
 * Маппер для должностного лица
 * @author Kashonkov Nikita
 */
@Mapper
abstract class SignerMapper : BaseMapper<SignerEntity, Signer>() {
    companion object {
        /**
         * Переменная для создания маппера
         */
        val INSTANCE: SignerMapper = Mappers.getMapper(SignerMapper::class.java)
    }
}