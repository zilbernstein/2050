package ru.digipeople.locotech.inspector.api.model.response

import ru.digipeople.locotech.core.data.api.entity.MeasurementsEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Класс MeasurementsResponse - модель ответа метода work_measurements МП Выпуск на линию
 * @author Sostavkin Grisha
 */
class MeasurementsResponse : BaseCollectionResponse<MeasurementsEntity>()