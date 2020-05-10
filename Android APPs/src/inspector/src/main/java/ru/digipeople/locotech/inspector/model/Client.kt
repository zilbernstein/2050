package ru.digipeople.locotech.inspector.model

/**
 * Модель пользователя
 * @author Kashonkov Nikita
 */
class Client {
    /**
     * Имя
     */
    lateinit var name: String
    /**
     * Фамилия
     */
    lateinit var lastName: String
    /**
     * ФИО
     */
    lateinit var fio: String
    /**
     * Текущее оборудование
     */
    var equipment: SelectedEquipment? = null
    /**
     * Роль
     */
    lateinit var role: UserRole
    /**
     * Электронный адрес для печати
     */
    var emailForPrint: String? = null
    /**
     * Основной электронный адрес
     */
    var emailMain: String? = null

}