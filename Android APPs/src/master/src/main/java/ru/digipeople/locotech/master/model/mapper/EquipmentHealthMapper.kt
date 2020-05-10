package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.EquipmentHealthEntity
import ru.digipeople.locotech.master.model.EquipmentHealth
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер состояния оборудования
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [EquipmentHealthStatusMapper::class, EquipmentHealthTypeMapper::class])
abstract class EquipmentHealthMapper: BaseMapper<EquipmentHealthEntity, EquipmentHealth>(){
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: EquipmentHealthMapper = Mappers.getMapper(EquipmentHealthMapper::class.java)
    }
}