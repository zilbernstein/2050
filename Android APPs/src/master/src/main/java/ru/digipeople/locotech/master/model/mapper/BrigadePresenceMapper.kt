package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.api.model.BrigadePresenceEntity
import ru.digipeople.locotech.master.model.BrigadePresence
/**
 *  Маппер явки бригад
 */
@Mapper(uses = [WorkerPresenceMapper::class])
abstract class BrigadePresenceMapper : BaseMapper<BrigadePresenceEntity, BrigadePresence>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: BrigadePresenceMapper = Mappers.getMapper(BrigadePresenceMapper::class.java)
    }
}