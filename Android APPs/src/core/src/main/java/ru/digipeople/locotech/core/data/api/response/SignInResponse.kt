package ru.digipeople.locotech.core.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * Ответ на метод signIn - вход в систему
 */
class SignInResponse {
    /**
     * Имя пользователя
     */
    @SerializedName("first_name")
    var firstName = ""
    /**
     * Фамилия пользователя
     */
    @SerializedName("last_name")
    var lastName = ""
    /**
     * ФИО пользователя
     */
    @SerializedName("user_fio")
    var fio = ""
    /**
     * ФИО пользователя
     */
    @SerializedName("is_in_shift")
    var isInShift = false
    /**
     * Email для печати
     */
    @SerializedName("email_for_print")
    var emailForPrint = ""
    /**
     * Email основной
     */
    @SerializedName("email_main")
    var emailMain = ""
    /**
     * Роль пользователя
     */
    @SerializedName("user_role")
    var userRole = ""
    /**
     * Выбранное оборудование
     */
    @SerializedName("selected_equipment_id")
    var selectedEquipmentId = ""
    /**
     * Доступное для выбора оборудование (секции)
     */
    @SerializedName("available_equipments")
    var availableEquipments = emptyList<Equipment>()

    /**
     * Модель оборудования
     */
    class Equipment {
        /**
         * UUID выбранного оборудования (секции)
         */
        @SerializedName("id")
        var id = ""
        /**
         * Наименование оборудования (секции)
         */
        @SerializedName("name")
        var name = ""
        /**
         * Тип оборудования
         */
        @SerializedName("type")
        var type = ""
    }
}