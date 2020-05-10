package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Модель значения параметра колесной пары
 *
 * @author Michael Solenov
 */
class WheelParamsValueEntity {
    /**
     * идентификатор параметра
     */
    @SerializedName("param_id")
    var id: String? = null
    /**
     * значение для левого колеса
     */
    @SerializedName("left_value")
    var leftValue: String? = null
    /**
     * значение для правого колеса
     */
    @SerializedName("right_value")
    var rightValue: String? = null
}