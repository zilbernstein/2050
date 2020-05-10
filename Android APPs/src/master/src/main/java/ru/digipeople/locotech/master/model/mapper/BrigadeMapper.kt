package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер бригад
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [WorkerForBrigadeMapper::class])
abstract class BrigadeMapper {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: BrigadeMapper = Mappers.getMapper(BrigadeMapper::class.java)
    }
}