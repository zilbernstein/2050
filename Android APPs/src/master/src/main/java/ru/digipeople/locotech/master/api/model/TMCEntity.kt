package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель ТМЦ
 *
 * @author Kashonkov Nikita
 */
open class TMCEntity {
    /**
     * ID ТМЦ
     */
    @SerializedName("tmc_id")
    lateinit var id: String
    /**
     * Название ТМЦ
     */
    @SerializedName("tmc_name")
    lateinit var name: String
    /**
     * Остаток ТМЦ на складе
     */
    @SerializedName("tmc_stock_rest")
    var stockRest: Double = 0.0
    /**
     * Остаток ТМЦ на участке
     */
    @SerializedName("tmc_workshop")
    var workshop: Double = 0.0
    /**
     * Единица измерения
     */
    @SerializedName("uom")
    var uom: String = ""
}