package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.response.RemarkSldResponse
import ru.digipeople.locotech.inspector.model.RemarkSldInfo
/**
 * Маппер для замечаний ОТК
 */
@Mapper(uses = [RemarkMapper::class])
abstract class RemarkSldInfoMapper : BaseMapper<RemarkSldResponse, RemarkSldInfo>()
/**
 * Создание маппера
 */
val remarkSldMapper: RemarkSldInfoMapper = Mappers.getMapper(RemarkSldInfoMapper::class.java)