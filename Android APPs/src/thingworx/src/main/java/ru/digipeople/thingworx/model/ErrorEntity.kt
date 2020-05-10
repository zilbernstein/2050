package ru.digipeople.thingworx.model

import com.google.gson.annotations.SerializedName

/**
 * Модель ошибки
 *
 * @author Kashonkov Nikita
 */
class ErrorEntity {
    /**
     * Код ошибки
     */
    @SerializedName("code")
    var code: Int = 0
    /**
     * Описание ошибки
     */
    @SerializedName("description")
    lateinit var description: String
}