package ru.digipeople.locotech.inspector.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Класс AuthResponse - модель ответа метода signin МП Выпуск на линию
 * @author Kashonkov Nikita
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
    /**
     * Выбранное оборудование для отображения секции при авторизации
     */
    @SerializedName("selected_equipment")
    var equipment: Equipment? = null
    /**
     * Электронный адрес для печати
     */
    @SerializedName("email_for_print")
    var emailForPrint: String? = null
    /**
     * Основной электронный адрес
     */
    @SerializedName("email_main")
    var emailMain: String? = null
    /**
     *  Роль
     */
    @SerializedName("userrole")
    var role: String? = null

    class Equipment{
        /**
         * Номер секции, нужно для первичного отображения меню
         */
        @SerializedName("equipement")
        var equipment = ""
    }
}