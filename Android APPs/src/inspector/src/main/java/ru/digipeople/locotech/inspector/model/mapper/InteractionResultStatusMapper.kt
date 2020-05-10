package ru.digipeople.locotech.inspector.model.mapper

import ru.digipeople.locotech.master.model.InteractionResultStatus
import org.mapstruct.Mapper

/**
 * Маппер для результата взаимодействия
 * @author Kashonkov Nikita
 */
@Mapper
open class InteractionResultStatusMapper {
    /**
     * Преобразование сущности в модель
     */
    fun entityToModel(entity: Boolean): InteractionResultStatus? {
        if (entity) {
            return InteractionResultStatus.RESULT_OK
        } else {
            return InteractionResultStatus.RESULT_MISTAKE
        }
    }
}