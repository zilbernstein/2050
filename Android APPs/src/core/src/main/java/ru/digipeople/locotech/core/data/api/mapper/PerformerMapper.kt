package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity
import ru.digipeople.locotech.core.data.model.Performer

/**
 * Маппер для сущноти [PerformerEntity]
 *
 * @author Kashonkov Nikita
 */
@Mapper
abstract class PerformerMapper: BaseMapper<PerformerEntity, Performer>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: PerformerMapper = Mappers.getMapper(PerformerMapper::class.java)
    }
}