package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.metrologist.data.api.entity.WheelPairShortEntity
import ru.digipeople.locotech.metrologist.data.model.WheelPairShort

/**
 * Маппер колесной пары (короткой)
 *
 * @author Michael Solenov
 */
@Mapper
interface WheelPairShortMapper: BaseMapper<WheelPairShortEntity, WheelPairShort>