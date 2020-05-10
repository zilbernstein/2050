package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.metrologist.data.api.entity.ReportEntity
import ru.digipeople.locotech.metrologist.data.model.Report
/**
 * Маппер отчета
 */
@Mapper
interface ReportMapper : BaseMapper<ReportEntity, Report>
/**
 * Создание маппера
 */
val reportMapper: ReportMapper = Mappers.getMapper(ReportMapper::class.java)