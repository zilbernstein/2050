package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.technologist.api.response.AuthResponse
import ru.digipeople.locotech.technologist.mapper.base.BaseMapper
import ru.digipeople.locotech.technologist.model.Client

/**
 * Маппер для пользователя
 *
 * @author Sostavkin Grisha
 */
@Mapper
abstract class ClientMapper: BaseMapper<AuthResponse, Client>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: ClientMapper = Mappers.getMapper(ClientMapper::class.java)
    }
}