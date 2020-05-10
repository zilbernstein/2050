package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import ru.digipeople.locotech.metrologist.data.api.entity.ParameterEntity
import ru.digipeople.locotech.metrologist.data.model.*
import java.util.*
/**
 * Маппер параметра
 *
 * @author Michael Solenov
 */
@Mapper(uses = [ParameterTypeMapper::class])
abstract class ParameterMapper : BaseMapper<ParameterEntity, Parameter<*>> {
    override fun entityToModel(entity: ParameterEntity?): Parameter<*>? {
        if (entity == null) {
            return null
        }
        /**
         * Определение типа параметра
         */
        val parameter = when (parameterTypeMapper.entityToModel(entity.type)) {
            ParameterType.BOOLEAN -> BooleanParameter().apply {
                value = stringToBoolean(entity.value)
            }
            ParameterType.DATETIME -> DateTimeParameter().apply {
                value = stringToDate(entity.value)
            }
            ParameterType.NUMERIC -> NumericParameter().apply {
                value = stringToFloat(entity.value)
            }
            else -> TextParameter().apply {
                value = stringToString(entity.value)
            }
        }
        updateModelFromEntity(entity, parameter)
        return parameter
    }

    @Mapping(target = "value", ignore = true)
    abstract fun updateModelFromEntity(entity: ParameterEntity?, @MappingTarget parameter: Parameter<*>)

    fun modelToEntity(model: Parameter<*>?): ParameterEntity? {
        if (model == null) {
            return null
        }
        val parameter = ParameterEntity()
        parameter.value = when (model) {
            is BooleanParameter -> booleanToString(model.value)
            is DateTimeParameter -> dateToString(model.value)
            is NumericParameter -> floatToString(model.value)
            is TextParameter -> stringToString(model.value)
        }
        updateEntityFromModel(model, parameter)
        return parameter
    }

    @Mapping(target = "value", ignore = true)
    abstract fun updateEntityFromModel(parameter: Parameter<*>, @MappingTarget entity: ParameterEntity?)
    /**
     * строка - булевый тип
     */
    private fun stringToBoolean(value: String?): Boolean? {
        return value?.toBoolean()
    }
    /**
     * Булевый тип - строка
     */
    private fun booleanToString(value: Boolean?): String? {
        return value?.toString()
    }
    /**
     * Строка - дата
     */
    private fun stringToDate(value: String?): Date? {
        return value?.toLongOrNull()?.let { Date(it) }
    }
    /**
     * дата - строка
     */
    private fun dateToString(value: Date?): String? {
        return value?.toString()
    }
    /**
     * строка - вешественное число
     */
    private fun stringToFloat(value: String?): Float? {
        return value?.toFloatOrNull()
    }
    /**
     * вещественное число - строка
     */
    private fun floatToString(value: Float?): String? {
        return value?.toString()
    }
    /**
     * Строка - строка
     */
    private fun stringToString(value: String?): String? {
        return value
    }
}