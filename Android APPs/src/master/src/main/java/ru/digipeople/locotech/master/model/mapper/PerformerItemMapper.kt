package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.PerformerItem
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.api.model.PerformerItemEntity
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper

/**
 * Маппер исполнителя с флагом в работе
 * @author Kashonkov Nikita
 */
@Mapper(uses = [PerformerMapper::class])
abstract class PerformerItemMapper: BaseMapper<PerformerItemEntity, PerformerItem>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: PerformerItemMapper = Mappers.getMapper(PerformerItemMapper::class.java)
    }
}