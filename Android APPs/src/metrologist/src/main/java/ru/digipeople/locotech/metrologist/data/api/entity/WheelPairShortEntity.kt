package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
/**
 * Модель колесной пары (сокращенная)
 *
 * @author Michael Solenov
 */
class WheelPairShortEntity {
    /**
     * идентификатор КП
     */
    @SerializedName("pair_id")
    lateinit var pairId: String
    /**
     * номер КП
     */
    @SerializedName("pair_number")
    lateinit var pairNumber: String
    /**
     * номер оси
     */
    @SerializedName("axis_number")
    lateinit var axisNumber: String
    /**
     * номер левого бандажа
     */
    @SerializedName("flange_left_number")
    lateinit var flangeLeftNumber: String
    /**
     * номер правого бандажа
     */
    @SerializedName("flange_right_number")
    lateinit var flangeRigthNumber: String
    /**
     * признак необходимости обточки
     */
    @SerializedName("must_repair")
    var mustRepair: Boolean = false
    /**
     * идентификатор причины обточки
     */
    @SerializedName("repair_reason_id")
    lateinit var repairReasonId: String
    /**
     * наименование причины обточки
     */
    @SerializedName("repair_reason_name")
    lateinit var repairReasonName: String
}