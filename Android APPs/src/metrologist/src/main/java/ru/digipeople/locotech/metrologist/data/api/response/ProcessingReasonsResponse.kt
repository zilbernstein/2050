package ru.digipeople.locotech.metrologist.data.api.response

import ru.digipeople.locotech.metrologist.data.api.entity.ProcessingReasonEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Модель ответа метода processing_reasons МП Мониторинг КП
 *
 * @author Michael Solenov
 */
class ProcessingReasonsResponse: BaseCollectionResponse<ProcessingReasonEntity>()