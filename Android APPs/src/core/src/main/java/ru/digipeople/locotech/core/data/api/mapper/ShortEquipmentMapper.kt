package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.response.SelectSectionResponse
import ru.digipeople.locotech.core.data.api.response.SignInResponse
import ru.digipeople.locotech.core.data.model.ShortEquipment

/**
 * Маппер для [SignInResponse.Equipment]
 */
@Mapper(uses = [EquipmentTypeMapper::class])
interface ShortEquipmentMapper {
    fun fromSignInEquipment(entity: SignInResponse.Equipment): ShortEquipment
    fun fromSelectSectionEquipments(entities: List<SelectSectionResponse.Equipment>): List<ShortEquipment>

    companion object {
        /*
        * Создание маппера
        */
        fun get(): ShortEquipmentMapper = Mappers.getMapper(ShortEquipmentMapper::class.java)
    }
}