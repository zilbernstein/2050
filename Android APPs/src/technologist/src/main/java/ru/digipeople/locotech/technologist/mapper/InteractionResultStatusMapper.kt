package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import ru.digipeople.locotech.technologist.model.InteractionResultStatus

/**
 * Маппер для результата взаимодействия
 *
 * @author Sostavkin Grisha
 */
@Mapper
open class InteractionResultStatusMapper {
    fun entityToModel(entity: Boolean): InteractionResultStatus? {
        /*
        * Возвращает статус
        */
        if(entity) {
            return InteractionResultStatus.RESULT_OK
        } else {
            return InteractionResultStatus.RESULT_MISTAKE
        }
    }
}