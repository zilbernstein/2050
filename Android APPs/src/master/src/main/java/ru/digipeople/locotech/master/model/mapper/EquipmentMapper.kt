package ru.digipeople.locotech.master.model.mapper

import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.DateMapper
import ru.digipeople.locotech.core.data.model.EquipmentType
import ru.digipeople.locotech.core.data.model.WorksCounterViewType
import ru.digipeople.locotech.master.api.model.EquipmentEntity
import ru.digipeople.locotech.master.model.Equipment

/**
 *  Маппер оборудования
 */
@Mapper(uses = [SectionMapper::class, DateMapper::class])
abstract class EquipmentMapper : BaseMapper<EquipmentEntity, Equipment>() {
    @Mapping(target = "worksCount", ignore = true)
    abstract override fun entityToModel(entity: EquipmentEntity?): Equipment?

    @AfterMapping
    fun mapWorksCount(entity: EquipmentEntity, @MappingTarget model: Equipment) {
        val worksCountMap = mutableMapOf<WorksCounterViewType, Int>()
        for (section in entity.sectionList)
            /*
            * Количество работ работ в секции
            */
            section.worksCount?.forEach { workCount ->
                val workCounterViewType = WorksCounterViewType.of(workCount.type)
                val countInMap = worksCountMap[workCounterViewType] ?: 0
                worksCountMap[workCounterViewType] = countInMap + workCount.value
            }
        model.worksCount = worksCountMap
    }

    fun mapType(entity: String): EquipmentType? {
        return when (entity) {
            /*
            * Типы оборудования
            */
            "локомотив" -> EquipmentType.LOCOMOTIVE
            "Локомотив" -> EquipmentType.LOCOMOTIVE
            "линейное оборудование" -> EquipmentType.LINE_EQUIPMENT
            "Линейное оборудование" -> EquipmentType.LINE_EQUIPMENT
            else -> EquipmentType.UNDEFINED
        }
    }

    companion object {
        fun get(): EquipmentMapper = Mappers.getMapper(EquipmentMapper::class.java)
    }
}