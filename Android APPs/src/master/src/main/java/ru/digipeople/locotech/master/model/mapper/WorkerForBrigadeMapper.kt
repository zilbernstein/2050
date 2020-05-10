package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper

/**
 * Маппер исполнителя из бригады
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [PerformerMapper::class])
abstract class WorkerForBrigadeMapper {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkerForBrigadeMapper = Mappers.getMapper(WorkerForBrigadeMapper::class.java)
    }
}