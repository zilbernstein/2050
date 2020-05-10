package ru.digipeople.locotech.worker.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.core.data.api.mapper.BaseMapper
import ru.digipeople.locotech.worker.api.model.WorkEntity
import ru.digipeople.locotech.worker.model.Work

/**
 * Маппер для работы
 *
 * @author Kashonkov Nikita
 */
@Mapper(uses = [WorkStatusMapper::class])
abstract class WorkMapper : BaseMapper<WorkEntity, Work>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: WorkMapper = Mappers.getMapper(WorkMapper::class.java)
    }
}