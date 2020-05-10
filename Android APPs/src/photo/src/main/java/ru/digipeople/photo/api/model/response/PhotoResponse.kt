package ru.digipeople.photo.api.model.response

import ru.digipeople.photo.api.model.PhotoEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Ответ от ThingWorx на запрос фотографий
 *
 * @author Kashonkov Nikita
 */
class PhotoResponse: BaseCollectionResponse<PhotoEntity>()