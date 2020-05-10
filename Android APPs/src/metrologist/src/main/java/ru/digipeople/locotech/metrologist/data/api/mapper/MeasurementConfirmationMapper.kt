package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.metrologist.data.api.response.MeasurementInfoBeforeCompleteResponse
import ru.digipeople.locotech.metrologist.data.model.MeasurementInfoBeforeComplete

/**
 * Маппер подтверждения замера
 *
 * @author Michael Solenov
 */
@Mapper(uses = [
    MeasurementMapper::class,
    WheelPairShortMapper::class,
    RemarkMapper::class
])
interface MeasurementConfirmationMapper : BaseMapper<MeasurementInfoBeforeCompleteResponse, MeasurementInfoBeforeComplete>
/**
 * Создание маппера
 */
val measurementConfirmationMapper: MeasurementConfirmationMapper = Mappers.getMapper(MeasurementConfirmationMapper::class.java)
