package ru.digipeople.thingworx.model.response

import com.google.gson.annotations.SerializedName

/**
 * Модель ответа о результате взаимодейтсвия с платформой ThingWorx
 *
 * @author Kashonkov Nikita
 */
class ResultResponse {
    @SerializedName("result")
    var successful: Boolean = false
}