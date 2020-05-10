package ru.digipeople.locotech.master.api.model.request

import com.google.gson.annotations.SerializedName

/**
 * Модель параметров запроса на получение ремкомплекта
 */
class OrderToRepairRequest(
        /*
        * UUID секции
        */
        @SerializedName("section_id")
        val sectionId: String
)