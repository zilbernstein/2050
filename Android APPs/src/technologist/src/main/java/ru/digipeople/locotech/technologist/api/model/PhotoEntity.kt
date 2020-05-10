package ru.digipeople.locotech.technologist.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель фото
 *
 * @author Sostavkin Grisha
 */
class PhotoEntity {
    /**
     * Id фото
     */
    @SerializedName("id_photo")
    var id: String = ""
    /**
     * URL
     */
    @SerializedName("url_photo")
    var name: String = ""
}