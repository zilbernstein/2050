package ru.digipeople.photo.model.mapper

import org.mapstruct.Mapper
import ru.digipeople.photo.model.InteractionResultStatus

/**
 * Маппер для статуса результата взаимодействия
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class InteractionResultStatusMapper {
    fun entityToModel(entity: Boolean): InteractionResultStatus? {
        /*
        * получение статуса
        */
        if (entity) {
            return InteractionResultStatus.RESULT_OK
        } else {
            return InteractionResultStatus.RESULT_MISTAKE
        }
    }
}