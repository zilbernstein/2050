package ru.digipeople.locotech.master.helper.session.model

import ru.digipeople.locotech.core.data.model.ShortEquipment
/**
 *  Модель данных авторизации
 */
class SignInInfo {
    /**
     * Имя пользователя
     */
    var firstName = ""
    /**
     * Фамилия пользователя
     */
    var lastName = ""
    /**
     * ФИО пользователя
     */
    var fio = ""
    /**
     * Выбранное оборудование
     */
    var selectedEquipmentId = ""
    /**
     * Доступное для выбора оборудование (секции)
     */
    var availableEquipments = emptyList<ShortEquipment>()
}