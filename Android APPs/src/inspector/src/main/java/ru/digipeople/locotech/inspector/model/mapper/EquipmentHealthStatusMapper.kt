package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.inspector.model.EquipmentHealthStatus

/**
 * Маппер для статусов состояния оборудования
 * @author Kashonkov Nikita
 */
@Mapper
open class EquipmentHealthStatusMapper {
    /**
     * Преобразование сущности в модель
     */
    fun entityToModel(entity: String): EquipmentHealthStatus? {
        return EquipmentHealthStatus.getValue(entity)
    }

    fun modelToEntity(model: EquipmentHealthStatus): String {
        return model.description
    }
}