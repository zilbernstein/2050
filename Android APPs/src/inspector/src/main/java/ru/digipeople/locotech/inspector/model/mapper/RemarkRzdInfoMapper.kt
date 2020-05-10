package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.response.RemarkRzdResponse
import ru.digipeople.locotech.inspector.model.RemarkRzdInfo
/**
 * Маппер для замечаний РЖД
 */
@Mapper(uses = [RemarkMapper::class])
abstract class RemarkRzdInfoMapper : BaseMapper<RemarkRzdResponse, RemarkRzdInfo>()
/**
 * Создание маппера
 */
val remarkRzdMapper: RemarkRzdInfoMapper = Mappers.getMapper(RemarkRzdInfoMapper::class.java)