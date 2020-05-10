package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.BaseMapper
import ru.digipeople.locotech.worker.api.model.SectionEntity
import ru.digipeople.locotech.worker.model.Section

/**
 * Маппер для секций
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [WorkMapper::class])
abstract class SectionMapper: BaseMapper<SectionEntity, Section>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: SectionMapper = Mappers.getMapper(SectionMapper::class.java)
    }
}