package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.logger.LoggerFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Маппер даты
 *
 * @author Kashonkov Nikita
 */
@Mapper
open class WorkDateMapper {
    private val logger = LoggerFactory.getLogger(DateMapper::class)

    private val dateFormat = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return dateFormat
        }
    }
    /*
    * Функция возвращает значение даты
    */
    fun entityToModel(entity: String?): Date? {
        if (entity == null) {
            return null
        }
        return try {
            dateFormat.get().parse(entity)
        } catch (e: ParseException) {
            /*
            * сообщение об ошибке при парсинге даты
            */
            logger.error("Parse date error", e)
            null
        }

    }

    fun modelToEntity(model: Date?): String? {
        return if (model == null) null else dateFormat.get().format(model)
    }


    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: DateMapper = Mappers.getMapper(DateMapper::class.java)
    }
}