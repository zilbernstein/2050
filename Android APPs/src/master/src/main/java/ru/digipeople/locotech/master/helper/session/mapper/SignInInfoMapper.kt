package ru.digipeople.locotech.master.helper.session.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.ShortEquipmentMapper
import ru.digipeople.locotech.core.data.api.response.SignInResponse
import ru.digipeople.locotech.master.helper.session.model.SignInInfo
/**
 *  Маппер для метода авторизации
 */
@Mapper(uses = [ShortEquipmentMapper::class])
interface SignInInfoMapper {
    /*
    * Ответ метода авторизации
    */
    fun fromSignInResponse(signInResponse: SignInResponse): SignInInfo

    companion object {
        fun get(): SignInInfoMapper = Mappers.getMapper(SignInInfoMapper::class.java)
    }
}