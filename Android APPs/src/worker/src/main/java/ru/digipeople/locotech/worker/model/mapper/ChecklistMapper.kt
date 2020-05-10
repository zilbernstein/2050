package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.worker.api.model.response.ChecklistResponse
import ru.digipeople.locotech.worker.model.Checklist

/**
 * Маппер для чек-листа
 *
 * @author Sostavkin Grisha
 */
@Mapper
interface ChecklistMapper {
    fun fromEntity(entity: ChecklistResponse): Checklist
}
        /*
        * Создание маппреа
        */
val checklistMapper: ChecklistMapper = Mappers.getMapper(ChecklistMapper::class.java)
