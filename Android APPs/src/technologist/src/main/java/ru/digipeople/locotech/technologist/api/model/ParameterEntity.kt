package ru.digipeople.locotech.technologist.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель параметров
 *
 * @author Sostavkin Grisha
 */
class ParameterEntity {
    /**
     * Id параметра
     */
    @SerializedName("id_parameter")
    var id: String = ""
    /**
     * Название параметра
     */
    @SerializedName("parameter_name")
    var parameterName: String = ""
    /**
     * Значение параметра
     */
    @SerializedName("parameter_value")
    var parameterValue: String = ""
}