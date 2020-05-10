package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.CommentMapper
import ru.digipeople.locotech.inspector.api.model.ControlServiceOperationEntity
import ru.digipeople.locotech.inspector.model.ControlServiceOperation

@Mapper(uses = [WorkStatusMapper::class, CommentMapper::class])
abstract class ControlServiceOperationMapper: BaseMapper<ControlServiceOperationEntity, ControlServiceOperation>()
/**
 * Создание маппера
 */
val csoMapper: ControlServiceOperationMapper = Mappers.getMapper(ControlServiceOperationMapper::class.java)