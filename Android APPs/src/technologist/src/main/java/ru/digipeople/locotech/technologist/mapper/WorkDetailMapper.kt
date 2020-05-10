package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.technologist.api.response.WorkDetailResponse
import ru.digipeople.locotech.technologist.mapper.base.BaseMapper
import ru.digipeople.locotech.technologist.model.WorkDetail

/**
 * Маппер для деталей работы
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [ParameterMapper::class])
abstract class WorkDetailMapper: BaseMapper<WorkDetailResponse, WorkDetail>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkDetailMapper = Mappers.getMapper(WorkDetailMapper::class.java)
    }
}