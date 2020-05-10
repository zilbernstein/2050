package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.core.data.model.Stage

/**
 * Маппер для этапа замера
 */
@Mapper
abstract class StageTypeMapper {
    /*
            * возвращает значение этап
            */
    fun entityToModel(entity: Int): Stage {
        return Stage.valueOf(entity)!!
    }
}