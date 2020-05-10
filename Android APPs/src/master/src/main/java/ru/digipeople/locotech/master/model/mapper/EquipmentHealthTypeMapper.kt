package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.EquipmentHealthType
import org.mapstruct.Mapper

/**
 * Маппер типа состояния оборудования
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class EquipmentHealthTypeMapper {
    fun entityToModel(entity: String): EquipmentHealthType? {
        return EquipmentHealthType.getValue(entity)
    }
    /*
      * Функция возвращает поле описания модели
      */
    fun modelToEntity(model: EquipmentHealthType): String {
        return model.description
    }
}