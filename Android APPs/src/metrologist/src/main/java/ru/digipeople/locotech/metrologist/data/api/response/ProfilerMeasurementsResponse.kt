package ru.digipeople.locotech.metrologist.data.api.response

import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse
/**
 * Модель ответа метода profiler_measurements МП Мониторинг КП
 *
 * @author Michael Solenov
 */
class MeasurementResponse : BaseCollectionResponse<MeasurementEntity>() {}