package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.BaseMapper
import ru.digipeople.locotech.worker.api.model.TMCInWorkEntity
import ru.digipeople.locotech.worker.model.TMCInWork

/**
 * Маппер для ТМЦ
 *
 * @author Kashonkov Nikita
 */
@Mapper()
abstract class TMCInWorkMapper : BaseMapper<TMCInWorkEntity, TMCInWork>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: TMCInWorkMapper = Mappers.getMapper(TMCInWorkMapper::class.java)
    }
}