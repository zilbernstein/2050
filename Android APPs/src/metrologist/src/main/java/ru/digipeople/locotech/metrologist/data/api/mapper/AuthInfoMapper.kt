package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.metrologist.data.api.response.AuthResponse
import ru.digipeople.locotech.metrologist.data.model.AuthInfo

/**
 * Маппер информации о пользователе (ответ на успешно пройденную авторизацию)
 *
 * @author Michael Solenov
 */
@Mapper
interface AuthInfoMapper : BaseMapper<AuthResponse, AuthInfo> {
    companion object {
        /**
         * Создание переменной маппера
         */
        val INSTANCE: AuthInfoMapper = Mappers.getMapper(AuthInfoMapper::class.java)
    }
}