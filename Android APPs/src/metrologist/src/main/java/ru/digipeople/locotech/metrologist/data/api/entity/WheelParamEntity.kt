package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель параметра колесной пары
 *
 * @author Michael Solenov
 */
class WheelParamEntity {
    /**
     * идентификатор параметра
     */
    @SerializedName("param_id")
    lateinit var id: String
    /**
     * название параметра
     */
    @SerializedName("param_name")
    lateinit var name: String
    /**
     * тип данных параметра
     */
    @SerializedName("param_type")
    lateinit var type: String
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
    /**
     * нормативное значение
     */
    @SerializedName("norm_value")
    var normValue: String? = null
    /**
     * минимальное значение
     */
    @SerializedName("min_value")
    var minValue: String? = null
    /**
     * максимальное значение
     */
    @SerializedName("max_value")
    var maxValue: String? = null
    /**
     * дата изменения, в миллисекундах UTC
     */
    @SerializedName("date_change")
    var dateChange: Long = 0L
    /**
     * автор изменений
     */
    @SerializedName("user_change")
    lateinit var userChange: PerformerEntity
}