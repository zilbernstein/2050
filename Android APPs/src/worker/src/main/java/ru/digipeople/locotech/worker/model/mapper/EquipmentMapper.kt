package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.BaseMapper
import ru.digipeople.locotech.worker.api.model.EquipmentEntity
import ru.digipeople.locotech.worker.model.Equipment

/**
 * Маппер для оборудования
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [SectionMapper::class])
abstract class EquipmentMapper: BaseMapper<EquipmentEntity, Equipment>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: EquipmentMapper = Mappers.getMapper(EquipmentMapper::class.java)
    }
}