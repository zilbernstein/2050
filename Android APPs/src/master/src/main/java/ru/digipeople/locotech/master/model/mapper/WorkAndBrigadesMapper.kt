package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.api.model.WorksAndBrigadesEntity
import ru.digipeople.locotech.master.model.WorkAndBrigadeForAssignment
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

/**
 * Маппер работ и групп исполнителей
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [WorkGroupMapper::class, BrigadeMapper::class])
abstract class WorkAndBrigadesMapper: BaseMapper<WorksAndBrigadesEntity, WorkAndBrigadeForAssignment>() {
        companion object {
            /*
            * Переменная для создания маппера
            */
            val INSTANCE: WorkAndBrigadesMapper = Mappers.getMapper(WorkAndBrigadesMapper::class.java)
        }
}