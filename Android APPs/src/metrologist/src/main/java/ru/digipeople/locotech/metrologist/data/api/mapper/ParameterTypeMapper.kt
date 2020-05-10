package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.metrologist.data.model.ParameterType
/**
 * Маппер типа параметра
 *
 * @author Michael Solenov
 */
@Mapper
abstract class ParameterTypeMapper : BaseMapper<String, ParameterType> {
    override fun entityToModel(entity: String?): ParameterType? {
        /**
         * Определение типа
         */
        return when (entity) {
            "boolean" -> ParameterType.BOOLEAN
            "string" -> ParameterType.STRING
            "double" -> ParameterType.NUMERIC
            "date" -> ParameterType.DATETIME
            else -> null
        }
    }
}
/**
 * Создание маппера
 */
val parameterTypeMapper: ParameterTypeMapper = Mappers.getMapper(ParameterTypeMapper::class.java)
