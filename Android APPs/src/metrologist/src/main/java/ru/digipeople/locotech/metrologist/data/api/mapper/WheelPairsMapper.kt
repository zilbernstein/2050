package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.metrologist.data.api.entity.WheelPairEntity
import ru.digipeople.locotech.metrologist.data.model.WheelPair

/**
 * Маппер колесной пары (полной)
 *
 * @author Michael Solenov
 */
@Mapper(uses = [
    WheelParamMapper::class
])
interface WheelPairsMapper : BaseMapper<WheelPairEntity, WheelPair>
/**
 * Создание маппера
 */
val wheelPairsMapper: WheelPairsMapper = Mappers.getMapper(WheelPairsMapper::class.java)