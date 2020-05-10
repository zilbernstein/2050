package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.core.data.api.mapper.PerformerMapper

import ru.digipeople.locotech.metrologist.data.api.entity.RemarkEntity
import ru.digipeople.locotech.metrologist.data.model.Remark

/**
 * Маппер замечания
 *
 * @author Michael Solenov
 */
@Mapper(uses = [
    PerformerMapper::class,
    DateMapper::class
])
/**
 * Создание маппера
 */
interface RemarkMapper : BaseMapper<RemarkEntity, Remark>