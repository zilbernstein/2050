package ru.digipeople.locotech.metrologist.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * Модель ответа метода signin МП Мониторинг КП
 */
class AuthResponse {
    /**
     * Имя пользователя
     */
    @SerializedName("firstname")
    lateinit var firstName: String
    /**
     * Фамилия пользователя
     */
    @SerializedName("lastname")
    lateinit var lastName: String
    /**
     * ФИО пользователя
     */
    @SerializedName("fio_user")
    lateinit var fio: String
    /**
     * Id текущей секции пользователя
     */
    @SerializedName("current_section_id")
    var currentSectionId: String = ""
    /**
     * Наименование текущей секцияи пользователя
     */
    @SerializedName("current_section_name")
    var currentSectionName: String = ""
}