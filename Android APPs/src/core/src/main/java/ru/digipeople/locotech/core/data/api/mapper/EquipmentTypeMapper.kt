package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.core.data.model.EquipmentType

/**
 * Маппер для типа оборудования
 */
@Mapper
abstract class EquipmentTypeMapper {
    fun entityToModel(value: String?): EquipmentType? {
        /*
        * определение типа оборудования
        */
        return when (value) {
            "section" -> EquipmentType.LOCOMOTIVE
            "equipment" -> EquipmentType.LINE_EQUIPMENT
            else -> null
        }
    }
}
