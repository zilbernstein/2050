package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

class RepairTypeEntity {
    /**
     * Идентификатор
     */
    @SerializedName("id")
    var id = ""

    /**
     * Наименование
     */
    @SerializedName("name")
    var name = ""
}