package ru.digipeople.locotech.worker.api.model

import com.google.gson.annotations.SerializedName

/**
 * ТМЦ в работе
 *
 * @author Kashonkov Nikita
 */
class TMCInWorkEntity {
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
     * Количество ТМЦ запрошенное
     */
    @SerializedName("tmc_requested")
    var tmcRequested = 0.0
    /**
     * Количество ТМЦ по норме
     */
    @SerializedName("tmc_normal")
    var tmcNormal = 0.0
    /**
     * Единицы измерения
     */
    @SerializedName("uom")
    var uom: String = ""
}