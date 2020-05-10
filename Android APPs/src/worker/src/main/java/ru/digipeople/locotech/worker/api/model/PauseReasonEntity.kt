package ru.digipeople.locotech.worker.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель причины остановки работы
 *
 * @author Kashonkov Nikita
 */
class PauseReasonEntity {
    /**
     * Id причины остановки
     */
    @SerializedName("id_reason")
    lateinit var id: String
    /**
     * Название причины остановки
     */
    @SerializedName("reason_name")
    lateinit var reasonName: String
}