package ru.digipeople.locotech.master.model.mapper

import ru.digipeople.locotech.master.model.InteractionResult
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Маппер типа результата
 *
 * @author Kashonkov Nikita
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