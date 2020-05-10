package ru.digipeople.locotech.metrologist.data.api.response

import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementCategoryEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Модель ответа метода measurement_categories МП Мониторинг КП
 *
 * @author Kashonkov Nikita
 */
class MeasurementCategoriesResponse: BaseCollectionResponse<MeasurementCategoryEntity>() {
}