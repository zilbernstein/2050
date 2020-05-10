package ru.digipeople.photo.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель - фотография, приходящая от платформы ThingWorx
 *
 * @author Kashonkov Nikita
 */
class PhotoEntity {
    /**
     * Url фотографии
     */
    @SerializedName("url_photo")
    lateinit var url: String
    /**
     * Id фотографии
     */
    @SerializedName("id_photo")
    lateinit var id: String
}