package ru.digipeople.locotech.metrologist.data.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.metrologist.data.api.response.SectionsResponse
import ru.digipeople.locotech.metrologist.data.model.Locomotive
import ru.digipeople.locotech.metrologist.data.model.SectionState

/**
 * Маппер локомотивов
 */
@Mapper(uses = [RepairTypeMapper::class])
abstract class LocomotiveMapper : BaseMapper<SectionsResponse.Loco, Locomotive> {
    fun mapStateEntityToModel(entity: String) = SectionState.of(entity)

    companion object {
        /**
         * Создание переменной маппера
         */
        val INSTANCE: LocomotiveMapper = Mappers.getMapper(LocomotiveMapper::class.java)
    }
}