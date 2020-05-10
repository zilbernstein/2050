package ru.digipeople.photo.model

/**
 * Модель фотографии
 *
 * @author Kashonkov Nikita
 */
class Photo {
    /**
     * Url фотографии
     */
    lateinit var url: String
    /**
     * Id фотографии
     */
    lateinit var id: String
    /**
     * Флаг новой фотографии
     */
    var isNew = false
}