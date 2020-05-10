package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.EquipmentHealthStatus
import org.mapstruct.Mapper

/**
 * Маппер статуса состояния оборудования
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class EquipmentHealthStatusMapper {
    fun entityToModel(entity: String): EquipmentHealthStatus? {
        return EquipmentHealthStatus.getValue(entity)
    }
    /*
    * Функция возвращает поле описания модели
    */
    fun modelToEntity(model: EquipmentHealthStatus): String {
        return model.description
    }
}