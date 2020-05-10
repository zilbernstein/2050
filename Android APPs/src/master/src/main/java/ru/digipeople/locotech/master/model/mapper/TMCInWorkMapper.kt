package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.TMCInWorkEntity
import ru.digipeople.locotech.master.model.TMCInWork
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper

/**
 * Маппер ТМЦ в работе
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [PerformerMapper::class, TmcStatusMapper::class])
abstract class TMCInWorkMapper : BaseMapper<TMCInWorkEntity, TMCInWork>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: TMCInWorkMapper = Mappers.getMapper(TMCInWorkMapper::class.java)
    }
}