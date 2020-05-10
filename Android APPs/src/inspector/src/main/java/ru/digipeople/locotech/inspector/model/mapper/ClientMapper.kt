package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.response.AuthResponse
import ru.digipeople.locotech.inspector.model.Client
import ru.digipeople.locotech.inspector.model.SelectedEquipment
/**
 * Маппер для пользователя
 */
@Mapper(uses = [UserRoleMapper::class])
abstract class ClientMapper {
    abstract fun fromEntity(entity: AuthResponse?): Client?

    @Mapping(source = "equipment", target = "name")
    abstract fun fromEntity(entity: AuthResponse.Equipment?): SelectedEquipment?
}
/**
 * Создание маппера
 */
val clientMapper: ClientMapper = Mappers.getMapper(ClientMapper::class.java)