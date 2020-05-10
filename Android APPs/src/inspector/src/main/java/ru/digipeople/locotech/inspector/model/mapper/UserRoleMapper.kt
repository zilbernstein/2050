package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.model.UserRole
/**
 * Маппер для роли пользователя
 */
@Mapper
abstract class UserRoleMapper {
    fun fromEntity(entity: String?) = when (entity) {
        /**
         * инспектор отк
         */
        "sld" -> UserRole.SLD
        /**
         * инспектор ржд
         */
        "rzd" -> UserRole.RZD
        else -> null
    }
}
/**
 * Создание маппера
 */
val userRoleMapper: UserRoleMapper = Mappers.getMapper(UserRoleMapper::class.java)