package ru.digipeople.locotech.inspector.support.model

import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.inspector.model.UserRole

class SignInInfo {
    /**
     * Имя
     */
    var name: String = ""
    /**
     * Фамилия
     */
    var lastName: String = ""
    /**
     * ФИО
     */
    var fio: String = ""
    /**
     * Текущее оборудование
     */
    var selectedEquipmentId: String = ""
    /**
     * Роль
     */
    var role: UserRole = UserRole.UNDEFINED
    /**
     * Электронный адрес для печати
     */
    var emailForPrint: String? = null
    /**
     * Основной электронный адрес
     */
    var emailMain: String? = null
    /**
     *
     */
    var availableEquipments = emptyList<ShortEquipment>()
}