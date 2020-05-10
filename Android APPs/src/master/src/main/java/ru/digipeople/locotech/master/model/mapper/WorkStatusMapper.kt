package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.WorkStatus
import org.mapstruct.Mapper

/**
 * Маппер статуса работы
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class WorkStatusMapper {
    fun entityToModel(entity: Int): WorkStatus? {
       return WorkStatus.valueOf(entity)
    }
/*
/Функция возвращает значение кода из модели статуса работы
 */
    fun modelToEntity(model: WorkStatus): Int {
        return model.code
    }
}