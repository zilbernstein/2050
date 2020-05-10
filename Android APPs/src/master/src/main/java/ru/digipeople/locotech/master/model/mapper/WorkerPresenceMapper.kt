package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.master.api.model.WorkerPresenceEntity
import ru.digipeople.locotech.master.model.WorkerPresence
/**
 * Маппер явки сотрудника
 */
@Mapper(uses = [DateMapper::class])
abstract class WorkerPresenceMapper : BaseMapper<WorkerPresenceEntity, WorkerPresence>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkerPresenceMapper = Mappers.getMapper(WorkerPresenceMapper::class.java)
    }
}