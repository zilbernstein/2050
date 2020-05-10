package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.model.WorksCounterViewType
import ru.digipeople.locotech.master.api.model.SectionEntity
import ru.digipeople.locotech.master.api.model.WorksCountEntity
import ru.digipeople.locotech.master.model.Section
import ru.digipeople.locotech.master.model.SectionState

/**
 * Маппер секций
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [EquipmentHealthMapper::class, RepairTypeMapper::class])
abstract class SectionMapper : BaseMapper<SectionEntity, Section>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: SectionMapper = Mappers.getMapper(SectionMapper::class.java)
    }

    fun mapStateEntityToModel(entity: String) = SectionState.of(entity)
    /*
     * Количество работ в секции
     */
    fun mapWorksCountToMap(entities: List<WorksCountEntity>?): Map<WorksCounterViewType, Int> {
        return entities?.associate { WorksCounterViewType.of(it.type) to it.value } ?: emptyMap()
    }
}