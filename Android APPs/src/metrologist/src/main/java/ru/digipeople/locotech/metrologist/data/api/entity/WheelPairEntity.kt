package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Модель колесной пары (полная)
 *
 * @author Michael Solenov
 */
class WheelPairEntity {
    /**
     * идентификатор КП
     */
    @SerializedName("pair_id")
    lateinit var id: String
    /**
     * номер КП
     */
    @SerializedName("pair_number")
    lateinit var number: String
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
    lateinit var flangeRightNumber: String
    /**
     * признак необходимости обточки
     */
    @SerializedName("must_repair")
    val mustRepair: Boolean = false
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
    /**
     * параметры колёс
     */
    @SerializedName("wheel_params")
    lateinit var wheelParams: List<WheelParamEntity>
}