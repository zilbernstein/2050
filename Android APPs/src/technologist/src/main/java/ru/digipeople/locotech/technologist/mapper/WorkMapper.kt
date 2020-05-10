package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.technologist.api.model.WorkEntity
import ru.digipeople.locotech.technologist.mapper.base.BaseMapper
import ru.digipeople.locotech.technologist.model.Work

/**
 * Маппер для работы
 *
 * @author Sostavkin Grisha
 */
@Mapper
abstract class WorkMapper : BaseMapper<WorkEntity, Work>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkMapper = Mappers.getMapper(WorkMapper::class.java)
    }
}