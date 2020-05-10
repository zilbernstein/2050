package ru.digipeople.locotech.inspector.model.mapper

import ru.digipeople.locotech.master.model.InteractionResult
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Маппер для результата взаимодействия
 * @author Kashonkov Nikita
 */
@Mapper(uses = [InteractionResultStatusMapper::class])
abstract class InteractionResultMapper {
    @Mapping(source = "successful", target = "result")
    abstract fun entityToModel(entity: ResultResponse): InteractionResult
    /**
     * Переменная для создания маппера
     */
    companion object {
        val INSTANCE: InteractionResultMapper = Mappers.getMapper(InteractionResultMapper::class.java)
    }
}