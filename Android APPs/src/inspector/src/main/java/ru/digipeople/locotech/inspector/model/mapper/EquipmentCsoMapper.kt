package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.EquipmentCsoEntity
import ru.digipeople.locotech.inspector.model.EquipmentCso
/**
 * Маппер для контрольно сервисных операций
 */

@Mapper(uses = [ControlServiceOperationMapper::class])
abstract class EquipmentCsoMapper : BaseMapper<EquipmentCsoEntity, EquipmentCso>()
/**
 * Создание маппера
 */
val equipmentCSOMapper: EquipmentCsoMapper = Mappers.getMapper(EquipmentCsoMapper::class.java)