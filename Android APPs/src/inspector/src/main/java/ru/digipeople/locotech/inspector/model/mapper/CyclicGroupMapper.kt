package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.CyclicGroupEntity
import ru.digipeople.locotech.inspector.model.CyclicGroup
/**
 * Маппер группы цикловых работ
 */
@Mapper(uses = [WorkMapper::class])
abstract class CyclicGroupMapper : BaseMapper<CyclicGroupEntity, CyclicGroup>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: CyclicGroupMapper = Mappers.getMapper(CyclicGroupMapper::class.java)
    }
}