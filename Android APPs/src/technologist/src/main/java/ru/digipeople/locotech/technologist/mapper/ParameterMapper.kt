package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.technologist.api.model.ParameterEntity
import ru.digipeople.locotech.technologist.mapper.base.BaseMapper
import ru.digipeople.locotech.technologist.model.Parameter

/**
 * Маппер для параметров
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [WorkMapper::class, DateMapper::class])
abstract class ParameterMapper: BaseMapper<ParameterEntity, Parameter>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: ParameterMapper = Mappers.getMapper(ParameterMapper::class.java)
    }
}