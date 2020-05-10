package ru.digipeople.locotech.worker.model

import com.google.gson.annotations.SerializedName

/**
 * @author Mike Solenov
 */
class MeasurementChangeRequest(
        /**
         * Идентификатор показателя
         */
        @SerializedName("measurement_id")
        val measurementId: String,

        /**
         * Идентификатор характеристики
         */
        @SerializedName("characteristic_id")
        val characteristicId: String,

        /**
         * Этап
         */
        @SerializedName("measurement_stage")
        val measurementStage: Int = 0,

        /**
         * Значение показателя (должен уметь принимать булевые как стринги)
         */
        @SerializedName("measurement_value")
        val measurementValue: Any?,

        /**
         * Текст комментария
         */
        @SerializedName("comment")
        val comment: String?
)