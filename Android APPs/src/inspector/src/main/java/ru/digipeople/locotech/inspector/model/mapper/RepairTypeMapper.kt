package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.inspector.api.model.RepairTypeEntity
import ru.digipeople.locotech.inspector.model.RepairType
/**
 * Маппер типа ремонта
 */
@Mapper
abstract class RepairTypeMapper : BaseMapper<RepairTypeEntity, RepairType>()