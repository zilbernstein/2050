package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.InteractionResultStatus
import org.mapstruct.Mapper

/**
 * Маппер статуса ответа
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class InteractionResultStatusMapper {
    /*
      * Функция возвращает тип ответа (успешный или ошибка)
      */
    fun entityToModel(entity: Boolean): InteractionResultStatus? {
        if(entity) {
            return InteractionResultStatus.RESULT_OK
        } else {
            return InteractionResultStatus.RESULT_MISTAKE
        }
    }
}