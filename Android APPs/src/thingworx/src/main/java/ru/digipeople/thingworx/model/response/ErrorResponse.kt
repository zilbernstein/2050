package ru.digipeople.thingworx.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.thingworx.model.ErrorEntity

/**
 * Модель отевта ошибки
 *
 * @author Kashonkov Nikita
 */
class ErrorResponse{
    @SerializedName("Error")
    lateinit var error: ErrorEntity
}