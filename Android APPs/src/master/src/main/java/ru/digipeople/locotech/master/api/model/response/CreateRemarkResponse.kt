package ru.digipeople.locotech.master.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Модель ответа с сервера на запрос о создании замечания
 *
 * @author Kashonkov Nikita
 */
class CreateRemarkResponse {
    /**
     * Id экземпляра замечания
     */
    @SerializedName("id_remark")
    lateinit var remarkId: String
}