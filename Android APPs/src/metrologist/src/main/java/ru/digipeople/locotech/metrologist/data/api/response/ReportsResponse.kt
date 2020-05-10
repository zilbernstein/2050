package ru.digipeople.locotech.metrologist.data.api.response

import ru.digipeople.locotech.metrologist.data.api.entity.ReportEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Модель ответа метода reports МП Мониторинг КП
 *
 * @author Michael Solenov
 */
class ReportsResponse : BaseCollectionResponse<ReportEntity>()