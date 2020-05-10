package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель ТМЦ в работе
 *
 * @author Kashonkov Nikita
 */
class TMCInWorkEntity: TMCEntity(){
    /**
     * Статус
     */
    @SerializedName("tmc_status")
    lateinit var status: String
    /**
     * Получатель
     */
    @SerializedName("reciver")
    var reciver: PerformerEntity? = null
    /**
     * Количество ТМЦ запрошенное
     */
    @SerializedName("tmc_requested")
    var requested = 0.0
    /**
     * Количество ТМЦ по норме
     */
    @SerializedName("tmc_normal")
    var normal = 0.0
    /**
     * Установлено к списанию
     */
    @SerializedName("tmc_assigned")
    var assigned = 0.0
    /**
     * Минимально
     */
    @SerializedName("tmc_min")
    var min = 0.0
    /**
     * Максимально
     */
    @SerializedName("tmc_max")
    var max = 0.0
    /**
     * МНК
     */
    @SerializedName("tmc_mnk")
    var mnk = 0.0
}