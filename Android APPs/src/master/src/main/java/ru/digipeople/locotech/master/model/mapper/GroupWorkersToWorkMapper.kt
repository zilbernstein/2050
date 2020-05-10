package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.GroupWorkersToWorkEntity
import ru.digipeople.locotech.master.model.GroupWorkersToWork
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер г8руппы исполнителей для работы
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [WorkerToWorkMapper::class])
abstract class GroupWorkersToWorkMapper: BaseMapper<GroupWorkersToWork, GroupWorkersToWorkEntity>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: GroupWorkersToWorkMapper = Mappers.getMapper(GroupWorkersToWorkMapper::class.java)
    }
}