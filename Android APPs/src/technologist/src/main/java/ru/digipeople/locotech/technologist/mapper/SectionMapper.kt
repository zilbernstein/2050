package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.technologist.api.model.SectionEntity
import ru.digipeople.locotech.technologist.mapper.base.BaseMapper
import ru.digipeople.locotech.technologist.model.Section

/**
 * Маппер для секций
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [RemarkMapper::class])
abstract class SectionMapper: BaseMapper<SectionEntity, Section>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: SectionMapper = Mappers.getMapper(SectionMapper::class.java)
    }
}