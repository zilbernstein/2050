package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.RemarkFromCatalogEntity
import ru.digipeople.locotech.inspector.model.RemarkFromCatalog

/**
 * Маппер для замечания из списка замечаний
 * @author Kashonkov Nikita
 */
@Mapper
abstract class RemarkFromCatalogMapper : BaseMapper<RemarkFromCatalogEntity, RemarkFromCatalog>() {
    companion object {
        /**
         * Переменная для создания маппера
         */
        val INSTANCE: RemarkFromCatalogMapper = Mappers.getMapper(RemarkFromCatalogMapper::class.java)
    }
}