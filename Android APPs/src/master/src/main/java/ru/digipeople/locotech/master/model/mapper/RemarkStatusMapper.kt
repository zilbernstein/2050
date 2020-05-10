package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.RemarkStatus
import org.mapstruct.Mapper

/**
 * Маппер статуса замечания
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class RemarkStatusMapper {
    fun entityToModel(entity: Int): RemarkStatus? {
        return RemarkStatus.valueOf(entity)
    }
/*
* Функция возвращает значения кода из модели
*/
    fun modelToEntity(model: RemarkStatus): Int {
        return model.code
    }
}