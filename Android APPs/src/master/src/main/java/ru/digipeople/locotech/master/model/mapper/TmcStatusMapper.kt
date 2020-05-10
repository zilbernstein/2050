package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.TMCStatus
import org.mapstruct.Mapper

/**
 * Маппер статуса ТМЦ
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class TmcStatusMapper {
    fun entityToModel(description: String): TMCStatus {
        val status = TMCStatus.getBy(description)
        return  if (status == null) {
            TMCStatus.UNKNOWN
        } else {
            status!!
        }
    }
/*
* Функция возвращает значение поля описания
*/
    fun modelToEntity(status: TMCStatus): String {
        return status.description
    }
}