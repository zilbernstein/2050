package ru.digipeople.photo.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Ответ на запрос идентификатора доступной финги
 */
class ThingIdResponse {
    @SerializedName("thing_id")
    /**
     * id
     */
    lateinit var name: String
}