package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер группа работ
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [WorkForWorkerAssignmentMapper::class])
abstract class WorkGroupMapper {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkGroupMapper = Mappers.getMapper(WorkGroupMapper::class.java)
    }
}