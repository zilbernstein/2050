package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.BaseMapper
import ru.digipeople.locotech.worker.api.model.PauseReasonEntity
import ru.digipeople.locotech.worker.model.PauseReason

/**
 * Маппер для причин остановки работы
 *
 * @author Kashonkov Nikita
 */
@Mapper
abstract class ReasonMapper : BaseMapper<PauseReasonEntity, PauseReason>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: ReasonMapper = Mappers.getMapper(ReasonMapper::class.java)
    }
}