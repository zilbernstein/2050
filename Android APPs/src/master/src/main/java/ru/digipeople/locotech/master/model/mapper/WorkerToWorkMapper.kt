package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.WorkerToWorkEntity
import ru.digipeople.locotech.master.model.WorkerToWork
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер исполнителя для работы
 *
 * @author Sostavkin Grisha
 */
@Mapper
abstract class WorkerToWorkMapper: BaseMapper<WorkerToWork, WorkerToWorkEntity>()  {
        companion object {
            /*
        * Переменная для создания маппера
        */
            val INSTANCE: WorkerToWorkMapper = Mappers.getMapper(WorkerToWorkMapper::class.java)
        }
}