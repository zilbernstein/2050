package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Модель причины обточки
 *
 * @author Michael Solenov
 */
class ProcessingReasonEntity {
    /**
     * идентификатор причины
     */
    @SerializedName("reason_id")
    lateinit var id: String
    /**
     * название причины
     */
    @SerializedName("reason_name")
    lateinit var name: String
}