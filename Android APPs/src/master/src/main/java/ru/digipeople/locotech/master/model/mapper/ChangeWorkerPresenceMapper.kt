package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.master.api.model.response.ChangePresenceResponse
import ru.digipeople.locotech.master.model.WorkerPresence
/**
 *  Маппер яизменения явки сотрудника
 */
@Mapper(uses = [DateMapper::class])
abstract class ChangeWorkerPresenceMapper : BaseMapper<ChangePresenceResponse, WorkerPresence>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE = Mappers.getMapper(ChangeWorkerPresenceMapper::class.java)
    }
}