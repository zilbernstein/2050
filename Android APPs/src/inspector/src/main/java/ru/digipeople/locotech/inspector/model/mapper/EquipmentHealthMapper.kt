package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.EquipmentHealthEntity
import ru.digipeople.locotech.inspector.model.EquipmentHealth

/**
 * Маппер для состояния оборудования
 * @author Kashonkov Nikita
 */
@Mapper(uses = [EquipmentHealthStatusMapper::class, EquipmentHealthTypeMapper::class])
abstract class EquipmentHealthMapper : BaseMapper<EquipmentHealthEntity, EquipmentHealth>() {
    companion object {
        /**
         * Переменная для создания маппера
         */
        val INSTANCE: EquipmentHealthMapper = Mappers.getMapper(EquipmentHealthMapper::class.java)
    }
}