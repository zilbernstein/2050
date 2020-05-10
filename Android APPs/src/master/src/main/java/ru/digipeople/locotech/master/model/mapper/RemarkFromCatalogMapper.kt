package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.RemarkFromCatalog
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.api.model.RemarkFromCatalogEntity

/**
 * Маппер замечания из списка
 *
 * @author Kashonkov Nikita
 */
@Mapper
abstract class RemarkFromCatalogMapper: BaseMapper<RemarkFromCatalogEntity, RemarkFromCatalog>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: RemarkFromCatalogMapper = Mappers.getMapper(RemarkFromCatalogMapper::class.java)
    }
}