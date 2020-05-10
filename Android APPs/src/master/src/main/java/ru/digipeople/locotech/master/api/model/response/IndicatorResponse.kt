package ru.digipeople.locotech.master.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Модель характеристики оборудования
 *
 * @author Sostavkin Grisha
 */
class IndicatorResponse(
        /**
         * Идентиффикатор показателя
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
        val measureStage: Int,
        /**
         * Значение показателя
         */
        @SerializedName("measurement_value")
        val measureValue: Any,
        /**
         * Текст комментария
         */
        @SerializedName("comment")
        val comment: String)