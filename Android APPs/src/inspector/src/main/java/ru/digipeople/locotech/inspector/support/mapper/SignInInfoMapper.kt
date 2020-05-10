package ru.digipeople.locotech.inspector.support.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.ShortEquipmentMapper
import ru.digipeople.locotech.core.data.api.response.SignInResponse
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.inspector.support.model.SignInInfo

/**
 *  Маппер для метода авторизации
 */
@Mapper(uses = [ShortEquipmentMapper::class])
interface SignInInfoMapper {
    fun fromSignInResponse(authResponse: SignInResponse): SignInInfo
    /**
     * Создание маппера
     */
    companion object {
        fun get(): SignInInfoMapper = Mappers.getMapper(SignInInfoMapper::class.java)
    }
}