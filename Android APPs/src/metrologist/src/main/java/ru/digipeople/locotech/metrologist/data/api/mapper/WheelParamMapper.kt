package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper
import ru.digipeople.locotech.metrologist.data.api.entity.WheelParamEntity
import ru.digipeople.locotech.metrologist.data.api.entity.WheelParamsValueEntity
import ru.digipeople.locotech.metrologist.data.model.*
import java.util.*
/**
 * Маппер параметра колесной пары
 *
 * @author Michael Solenov
 */
@Mapper(uses = [
    ParameterTypeMapper::class,
    PerformerMapper::class
])
abstract class WheelParamMapper {
    fun fromEntity(entity: WheelParamEntity?): WheelParam<*>? {
        if (entity == null) {
            return null
        }/**
         * Определение типа параметра
         */
        val wheelParam = when (parameterTypeMapper.entityToModel(entity.type)) {
            ParameterType.BOOLEAN -> BooleanWheelParam().apply {
                fillTypedValues(entity, this, ::stringToBoolean)
            }
            ParameterType.DATETIME -> DateTimeWheelParam().apply {
                fillTypedValues(entity, this, ::stringToDate)
            }
            ParameterType.NUMERIC -> NumericWheelParam().apply {
                fillTypedValues(entity, this, ::stringToFloat)
            }
            else -> TextWheelParam().apply {
                fillTypedValues(entity, this, ::stringToString)
            }
        }
        updateModelFromEntity(entity, wheelParam)
        return wheelParam
    }
    /**
     * Определение типа значения
     */
    @Mappings(
            Mapping(target = "leftValue", ignore = true),
            Mapping(target = "rightValue", ignore = true),
            Mapping(target = "normValue", ignore = true),
            Mapping(target = "minValue", ignore = true),
            Mapping(target = "maxValue", ignore = true)
    )
    abstract fun updateModelFromEntity(entity: WheelParamEntity?, @MappingTarget wheelParam: WheelParam<*>)

    private fun <T> fillTypedValues(entity: WheelParamEntity, wheelParam: WheelParam<T>, mapFunc: (String?) -> T?) {
        wheelParam.leftValue = mapFunc(entity.leftValue)
        wheelParam.rightValue = mapFunc(entity.rightValue)
        wheelParam.normValue = mapFunc(entity.normValue)
        wheelParam.minValue = mapFunc(entity.minValue)
        wheelParam.maxValue = mapFunc(entity.maxValue)
    }

    abstract fun fromEntities(entities: List<WheelParamEntity?>): List<WheelParam<*>?>?
    /**
     * Преобразование к параметрам колесной пары
     */
    fun toWheelParamsValue(model: WheelParam<*>?): WheelParamsValueEntity? {
        if (model == null) {
            return null
        }
        val param = WheelParamsValueEntity()
        param.id = model.id
        when (model) {
            is BooleanWheelParam -> fillTypedValues(model, param, ::booleanToString)
            is DateTimeWheelParam -> fillTypedValues(model, param, ::dateToString)
            is NumericWheelParam -> fillTypedValues(model, param, ::floatToString)
            is TextWheelParam -> fillTypedValues(model, param, ::stringToString)
        }
        return param
    }

    private fun <T> fillTypedValues(wheelParam: WheelParam<T>, entity: WheelParamsValueEntity, mapFunc: (T?) -> String?) {
        entity.leftValue = mapFunc(wheelParam.leftValue)
        entity.rightValue = mapFunc(wheelParam.rightValue)
    }
    /**
     * Строка к булевому типы
     */
    private fun stringToBoolean(value: String?): Boolean? {
        return value?.toBoolean()
    }
    /**
     * Булевный тип к строке
     */
    private fun booleanToString(value: Boolean?): String? {
        return value?.toString()
    }
    /**
     * Строка к дате
     */
    private fun stringToDate(value: String?): Date? {
        return value?.toLongOrNull()?.let { Date(it) }
    }
    /**
     * Дата к строке
     */
    private fun dateToString(value: Date?): String? {
        return value?.toString()
    }
    /**
     * Строка к вещественному типу
     */
    private fun stringToFloat(value: String?): Float? {
        return value?.toFloatOrNull()
    }
    /**
     * Вещественный тип к строке
     */
    private fun floatToString(value: Float?): String? {
        return value?.toString()
    }
    /**
     * Строка к строке
     */
    private fun stringToString(value: String?): String? {
        return value
    }
}

val wheelParamMapper: WheelParamMapper = Mappers.getMapper(WheelParamMapper::class.java)