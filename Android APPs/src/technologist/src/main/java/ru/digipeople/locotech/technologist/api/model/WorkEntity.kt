package ru.digipeople.locotech.technologist.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель работы
 */
class WorkEntity {
    /**
     * Id работы
     */
    @SerializedName("id_work")
    var id: String = ""
    /**
     * Название работы
     */
    @SerializedName("work_name")
    var name: String = ""
    /**
     * Количество повторов
     */
    @SerializedName("repeats")
    var repeats = 0
}