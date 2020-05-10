package ru.digipeople.telephonebook.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель контакта
 *
 * @author Sostavkin Grisha
 */
class ContactEntity {
    /**
     * Id пользователя
     */
    @SerializedName("id_user")
    var id: String = ""
    /**
     * Имя абонента
     */
    @SerializedName("user_name")
    var name: String = ""
    /**
     * Телефон абонента
     */
    @SerializedName("user_phone")
    var phoneNumber: String = ""
    /**
     * Должность
     */
    @SerializedName("user_duty")
    var job: String = ""
}