package ru.digipeople.locotech.master.api.model.response

import com.google.gson.annotations.SerializedName

class OrderToRepairResponse {
    /**
     * Статус запроса на получение ремкомплекта
     */
    @SerializedName("status")
    lateinit var status: String
}