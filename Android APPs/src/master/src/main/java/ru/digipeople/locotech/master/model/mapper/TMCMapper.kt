package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.TMCEntity
import ru.digipeople.locotech.master.model.TMC
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер ТМЦ
 *
 * @author Kashonkov Nikita
 */
@Mapper
abstract class TMCMapper : BaseMapper<TMCEntity, TMC>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: TMCMapper = Mappers.getMapper(TMCMapper::class.java)
    }
}