package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.model.MeasureValueType

/**
 * Маппер для типа замера
 *
 * @author Michael Solenov
 */
@Mapper
abstract class MeasureValueTypeMapper {

    fun entityToModel(value: String?): MeasureValueType {
        /*
        * определение типа замера
        */
        return when (value) {
            "boolean" -> MeasureValueType.BOOLEAN
            else -> MeasureValueType.STRING
        }
    }

    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: MeasureValueTypeMapper = Mappers.getMapper(MeasureValueTypeMapper::class.java)
    }
}
