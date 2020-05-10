package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName

class RepairTypeEntity(
        /**
         * Идентификатор
         */
        @SerializedName("id")
        val id: String,

        /**
         * Наименование
         */
        @SerializedName("name")
        val name: String
)