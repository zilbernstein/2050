package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
/**
 * Модель параметра
 */
class ParameterEntity {
    /**
     * идентификатор параметра
     */
    @SerializedName("parameter_id")
    lateinit var id: String
    /**
     * название параметра
     */
    @SerializedName("parameter_name")
    lateinit var name: String
    /**
     * тип данных параметра
     */
    @SerializedName("parameter_type")
    lateinit var type: String
    /**
     * значение параметра
     */
    @SerializedName("parameter_value")
    var value: String? = null
    /**
     * порядок для сортировки
     */
    @SerializedName("parameter_order")
    var order: Int = 0

}
