package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

class RepairTypeEntity {
    /*
    * UUID типа ремонта
    */
    @SerializedName("id")
    var id = ""

    /**
     * Наименование типа ремонта
     */
    @SerializedName("name")
    var name = ""
}