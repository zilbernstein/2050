package ru.digipeople.locotech.metrologist.data.api.response

import ru.digipeople.locotech.metrologist.data.api.entity.WheelPairEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Модель ответа метода wheel_pairs МП Мониторинг КП
 *
 * @author Michael Solenov
 */
class WheelPairsResponse : BaseCollectionResponse<WheelPairEntity>()