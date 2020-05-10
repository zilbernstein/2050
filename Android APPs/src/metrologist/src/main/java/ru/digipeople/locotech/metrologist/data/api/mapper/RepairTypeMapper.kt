package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.metrologist.data.api.entity.RepairTypeEntity
import ru.digipeople.locotech.metrologist.data.model.RepairType

@Mapper
abstract class RepairTypeMapper: BaseMapper<RepairTypeEntity, RepairType>