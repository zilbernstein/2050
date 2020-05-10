package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.technologist.api.model.RemarkEntity
import ru.digipeople.locotech.technologist.mapper.base.BaseMapper
import ru.digipeople.locotech.technologist.model.Remark

/**
 * Маппер для замечаний
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [WorkMapper::class, DateMapper::class])
abstract class RemarkMapper: BaseMapper<RemarkEntity, Remark>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: RemarkMapper = Mappers.getMapper(RemarkMapper::class.java)
    }
}