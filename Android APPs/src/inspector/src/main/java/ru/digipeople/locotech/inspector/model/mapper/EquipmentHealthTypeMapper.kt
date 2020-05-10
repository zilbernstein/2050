package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.inspector.model.EquipmentHealthType

/**
 * Маппер для типа состояния оборудования
 * @author Kashonkov Nikita
 */
@Mapper
open class EquipmentHealthTypeMapper {
    /**
     * Преобразование сущности в модель
     */
    fun entityToModel(entity: String): EquipmentHealthType? {
        return EquipmentHealthType.getValue(entity)
    }

    fun modelToEntity(model: EquipmentHealthType): String {
        return model.description
    }
}