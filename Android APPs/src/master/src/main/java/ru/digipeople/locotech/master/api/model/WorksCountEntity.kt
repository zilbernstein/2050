package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 *  Модель числа работ во вкладке
 */
class WorksCountEntity(
        /**
         * Тип вкладки
         */
        @SerializedName("type")
        val type: String,

        /**
         * Значение количества работ
         */
        @SerializedName("value")
        val value: Int
)
