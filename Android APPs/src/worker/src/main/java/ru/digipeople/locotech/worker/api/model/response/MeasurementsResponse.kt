package ru.digipeople.locotech.worker.api.model.response

import ru.digipeople.locotech.core.data.api.entity.MeasurementsEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Класс [MeasurementsResponse] - модель ответа метода work_measurements МП Сотрудник
 *
 *@author Mike Solenov
 */
class MeasurementsResponse : BaseCollectionResponse<MeasurementsEntity>()