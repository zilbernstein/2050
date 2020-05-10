package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.master.api.model.RepairTypeEntity
import ru.digipeople.locotech.master.model.RepairType

/*
* Маппер типа ремонта
*/
@Mapper
abstract class RepairTypeMapper : BaseMapper<RepairTypeEntity, RepairType>()