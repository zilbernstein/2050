package ru.digipeople.locotech.core.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import java.util.*

/**
 * Маппер для даты
 */
@Mapper
open class DateMapper {
    /**
     * возвращает дату
     */
    fun entityToModel(entity: Long): Date {
        return Date(entity)
    }
    /**
     * возвращает время в мс
     */
    fun modelToEntity(date: Date): Long {
        return date.time
    }

    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: DateMapper = Mappers.getMapper(DateMapper::class.java)
    }
}