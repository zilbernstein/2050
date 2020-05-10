package ru.digipeople.locotech.inspector.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.locotech.inspector.api.model.DocumentEntity
import ru.digipeople.locotech.inspector.model.Document
/**
 * Маппер для документов
 */
@Mapper(uses = [WorkMapper::class])
abstract class DocumentsMapper : BaseMapper<DocumentEntity, Document>() {
    companion object {
        /**
         * Переменная для создания маппера
         */
        val INSTANCE: DocumentsMapper = Mappers.getMapper(DocumentsMapper::class.java)
    }
}