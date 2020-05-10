package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.core.data.api.mapper.CommentMapper
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.inspector.api.model.RemarkEntity
import ru.digipeople.locotech.inspector.model.Remark
/**
 * Маппер для замечаний
 */
@Mapper(uses = [WorkMapper::class, CommentMapper::class, DateMapper::class])
abstract class RemarkMapper : BaseMapper<RemarkEntity, Remark>()

