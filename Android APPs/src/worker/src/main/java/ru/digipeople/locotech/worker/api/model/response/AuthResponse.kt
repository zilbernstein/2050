package ru.digipeople.locotech.worker.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Класс [AuthResponse] - модель ответа метода signin_worker МП Сотрудник
 *
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
     * Название текущей работы
     */
    @SerializedName("work_name")
    var workName = ""
    /**
     * Id текущей работы
     */
    @SerializedName("id_work")
    var workId = ""
    /**
     * Флаг того что работник находится в смене
     */
    @SerializedName("is_in_shift")
    var isInShift = false
}