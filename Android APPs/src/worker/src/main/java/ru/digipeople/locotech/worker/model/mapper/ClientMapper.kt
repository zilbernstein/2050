package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.BaseMapper
import ru.digipeople.locotech.worker.api.model.response.AuthResponse
import ru.digipeople.locotech.worker.model.Client

/**
 * Маппер для пользователя
 *
 * @author Kashonkov Nikita
 */
@Mapper
abstract class ClientMapper : BaseMapper<AuthResponse, Client>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: ClientMapper = Mappers.getMapper(ClientMapper::class.java)
    }
}