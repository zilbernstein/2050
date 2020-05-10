package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.core.data.model.EquipmentType
import ru.digipeople.locotech.core.data.model.WorksCounterViewType
import ru.digipeople.locotech.inspector.api.model.EquipmentEntity
import ru.digipeople.locotech.inspector.model.Equipment
/**
 * Маппер для оборудования
 */
@Mapper(uses = [SectionMapper::class, DateMapper::class])
abstract class EquipmentMapper : BaseMapper<EquipmentEntity, Equipment>() {
    /**
     * Преобразование числа работ
     */
    @AfterMapping
    fun mapWorksCount(entity: EquipmentEntity, @MappingTarget model: Equipment) {
        val worksCountMap = mutableMapOf<WorksCounterViewType, Int>()
        for (section in entity.sections)
            section.worksCount?.forEach { workCount ->
                val workCounterViewType = WorksCounterViewType.of(workCount.type)
                val countInMap = worksCountMap[workCounterViewType] ?: 0
                worksCountMap[workCounterViewType] = countInMap + workCount.value
            }
        model.worksCount = worksCountMap
    }
    /**
     * Преобразование типа оборудования
     */
    fun entityToModel(entity: String): EquipmentType? {
        return when (entity) {
            "локомотив" -> EquipmentType.LOCOMOTIVE
            "Локомотив" -> EquipmentType.LOCOMOTIVE
            "линейное оборудование" -> EquipmentType.LINE_EQUIPMENT
            "Линейное оборудование" -> EquipmentType.LINE_EQUIPMENT
            else -> null
        }
    }
    /**
     * Создание маппера
     */
    companion object {
        fun get(): EquipmentMapper = Mappers.getMapper(EquipmentMapper::class.java)
    }
}