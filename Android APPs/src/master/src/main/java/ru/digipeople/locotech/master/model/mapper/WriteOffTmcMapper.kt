package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.WriteOffTmcEntity
import ru.digipeople.locotech.master.model.WriteOffTmc
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper

/**
 * Маппер ТМЦ для списания
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [PerformerMapper::class, TMCInWorkMapper::class])
abstract class WriteOffTmcMapper: BaseMapper<WriteOffTmcEntity, WriteOffTmc>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WriteOffTmcMapper = Mappers.getMapper(WriteOffTmcMapper::class.java)
    }
}