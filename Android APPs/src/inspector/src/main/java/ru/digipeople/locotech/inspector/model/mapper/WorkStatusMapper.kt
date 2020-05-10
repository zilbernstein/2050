package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.inspector.model.WorkStatus
/**
 * Маппер для статуса работы
 */
@Mapper
open class WorkStatusMapper {
    /**
     * Преобразовнаие сущности в модель
     */
    fun entityToModel(entity: Int): WorkStatus? {
        return WorkStatus.valueOf(entity)
    }

    fun modelToEntity(model: WorkStatus): Int {
        return model.code
    }
}