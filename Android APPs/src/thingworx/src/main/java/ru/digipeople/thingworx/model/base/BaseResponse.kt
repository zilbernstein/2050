package ru.digipeople.thingworx.model.base

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * Базовая модель ответа сервера
 *
 * @author Kashonkov Nikita
 */
class BaseResponse{
    @SerializedName("data")
    lateinit var response: JsonObject
}