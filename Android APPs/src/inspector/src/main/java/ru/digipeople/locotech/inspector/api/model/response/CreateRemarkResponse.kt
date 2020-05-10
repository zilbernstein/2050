package ru.digipeople.locotech.inspector.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Класс ControlPointsResponse - модель ответа метода add_remark и create_add_remark МП Выпуск на линию
 * @author Kashonkov Nikita
 */
class CreateRemarkResponse {
    /**
     * Id экземпляра замечания
     */
    @SerializedName("id_remark")
    lateinit var remarkId: String
}