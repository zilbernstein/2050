package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер работа для исполнителя
 *
 * @author Sostavkin Grisha
 */
@Mapper
abstract class WorkForWorkerAssignmentMapper {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkForWorkerAssignmentMapper = Mappers.getMapper(WorkForWorkerAssignmentMapper::class.java)
    }
}