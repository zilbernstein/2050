package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.worker.model.WorkStatus

/**
 * Маппер для статуса работы
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class WorkStatusMapper {
    fun entityToModel(entity: Int): WorkStatus? {
        return WorkStatus.valueOf(entity)
    }
    /*
            * Получение значения кода
            */
    fun modelToEntity(model: WorkStatus): Int {
        return model.code
    }
}