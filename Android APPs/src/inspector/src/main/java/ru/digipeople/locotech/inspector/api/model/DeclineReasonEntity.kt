package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель причины удаления замечания
 *
 * @author Sostavkin Grisha
 */
class DeclineReasonEntity {
    /**
     * Id причины
     */
    @SerializedName("id_reason")
    lateinit var id: String
    /**
     * Название причины
     */
    @SerializedName("reason_name")
    lateinit var name: String
}