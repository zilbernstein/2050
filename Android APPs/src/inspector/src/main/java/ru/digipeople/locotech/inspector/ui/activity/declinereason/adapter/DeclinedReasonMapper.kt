package ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.DeclineReasonEntity
import ru.digipeople.locotech.inspector.model.mapper.BaseMapper

/**
 * Маппер причин удаления замечания
 * @author Sostavkin Grisha
 */
@Mapper
abstract class DeclinedReasonMapper: BaseMapper<DeclineReasonEntity, DeclinedReasonModel>() {
    companion object {
        /**
         * Создание переменной маппера
         */
        val INSTANCE: DeclinedReasonMapper = Mappers.getMapper(DeclinedReasonMapper::class.java)
    }
}