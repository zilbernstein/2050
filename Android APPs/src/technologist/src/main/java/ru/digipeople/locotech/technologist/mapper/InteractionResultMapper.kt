package ru.digipeople.locotech.technologist.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.technologist.model.InteractionResult
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Маппер для модели ответа о результате взаимодейтсвия с платформой ThingWorx
 *
 * @author Sostavkin Grisha
 */
@Mapper(uses = [InteractionResultStatusMapper::class])
abstract class InteractionResultMapper {
    @Mapping(source = "successful", target = "result")
    abstract fun entityToModel(entity: ResultResponse): InteractionResult

    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: InteractionResultMapper = Mappers.getMapper(InteractionResultMapper::class.java)
    }
}