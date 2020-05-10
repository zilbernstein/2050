package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.technologist.api.model.EquipmentEntity
import ru.digipeople.locotech.technologist.mapper.base.BaseMapper
import ru.digipeople.locotech.technologist.model.Equipment

/**
 * Маппер для оборудования
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [SectionMapper::class])
abstract class EquipmentMapper : BaseMapper<EquipmentEntity, Equipment>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: EquipmentMapper = Mappers.getMapper(EquipmentMapper::class.java)
    }
}