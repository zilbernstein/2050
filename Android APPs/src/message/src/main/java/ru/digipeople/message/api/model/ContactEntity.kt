package ru.digipeople.message.api.model

import com.google.gson.annotations.SerializedName

/**
 * Сущность контакта с сервера
 *
 * @author Kashonkov Nikita
 */
class ContactEntity {
    /**
     * Id контакта
     */
    @SerializedName("id")
    var id = ""
    /**
     * ФИО контакта
     */
    @SerializedName("fio")
    var name = ""
}