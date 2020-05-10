package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.response.CyclicWorkResponse
import ru.digipeople.locotech.inspector.model.CyclicGroupsInfo
/**
 * Маппер для цикловых работ
 */
@Mapper(uses = [CyclicGroupMapper::class])
abstract class CyclicInfoMapper : BaseMapper<CyclicWorkResponse, CyclicGroupsInfo>()
/**
 * Создание маппера
 */
val cyclicMapper: CyclicInfoMapper = Mappers.getMapper(CyclicInfoMapper::class.java)