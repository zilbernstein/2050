package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.WorkFromCatalog
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.master.api.model.WorkFromCatalogEntity

/**
 * Маппер работы из списка
 *
 * @author Kashonkov Nikita
 */
@Mapper
abstract class WorkFromCatalogMapper: BaseMapper<WorkFromCatalogEntity, WorkFromCatalog>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkFromCatalogMapper = Mappers.getMapper(WorkFromCatalogMapper::class.java)
    }
}