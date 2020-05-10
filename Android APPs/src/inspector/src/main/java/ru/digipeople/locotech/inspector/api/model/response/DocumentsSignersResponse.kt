package ru.digipeople.locotech.inspector.api.model.response

import ru.digipeople.locotech.inspector.api.model.DocumentSignerEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Класс DocumentsSignersResponse - модель ответа метода documents_signers МП Выпуск на линию
 * @author Aleksandr Brazhkin
 */
class DocumentsSignersResponse : BaseCollectionResponse<DocumentSignerEntity>() {
}