package ru.digipeople.photo.model.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.digipeople.photo.api.model.PhotoEntity
import ru.digipeople.photo.model.Photo

/**
 * Маппер для фото
 *
 * @author Kashonkov Nikita
 */
@Mapper
abstract class PhotoMapper: BaseMapper<PhotoEntity, Photo>() {
    companion object {
        /*
        * Переменная для создания маппера
        */
        val INSTANCE: PhotoMapper = Mappers.getMapper(PhotoMapper::class.java)
    }
}