package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.api.model.AssignmentTemplateEntity
import ru.digipeople.locotech.master.model.AssignmentTemplate
/**
 *  Маппер шаблонов
 */
@Mapper
abstract class AssignmentTemplatesMapper : BaseMapper<AssignmentTemplateEntity, AssignmentTemplate>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: AssignmentTemplatesMapper = Mappers.getMapper(AssignmentTemplatesMapper::class.java)
    }
}