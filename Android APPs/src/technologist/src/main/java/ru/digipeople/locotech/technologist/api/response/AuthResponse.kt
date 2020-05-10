package ru.digipeople.locotech.technologist.api.response

import com.google.gson.annotations.SerializedName

/**
 * Ответ с сервера метода авторизации
 *
 * @author Sostavkin Grisha
 */
class AuthResponse {
    /**
     * Имя
     */
    @SerializedName("firstname")
    lateinit var name: String
    /**
     * Фамилия
     */
    @SerializedName("lastname")
    lateinit var lastName: String
    /**
     * ФИО
     */
    @SerializedName("fio_user")
    lateinit var fio: String
}