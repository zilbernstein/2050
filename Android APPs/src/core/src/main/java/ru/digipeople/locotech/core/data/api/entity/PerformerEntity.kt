package ru.digipeople.locotech.core.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Персона/человек
 */
class PerformerEntity {
    /**
     * Id
     */
    @SerializedName("id")
    lateinit var id: String
    /**
     * Имя
     */
    @SerializedName("fio")
    lateinit var name: String
}