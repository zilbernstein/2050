package ru.digipeople.locotech.inspector.api.model.response

import ru.digipeople.locotech.inspector.api.model.SignerItemEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Класс SignerListResponse - модель ответа метода workers_for_work МП Выпуск на линию
 * @author Kashonkov Nikita
 */
class SignerResponse : BaseCollectionResponse<SignerItemEntity>()