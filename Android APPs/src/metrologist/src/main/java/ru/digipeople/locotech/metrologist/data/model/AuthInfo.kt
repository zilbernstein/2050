package ru.digipeople.locotech.metrologist.data.model

/**
 * Модель информации о пользователе (получаем после успешной авторизации)
 *
 * @author Michael Solenov
 */
class AuthInfo {
    /**
     * Имя пользователя
     */
    lateinit var firstName: String
    /**
     * Фамилия пользователя
     */
    lateinit var lastName: String
    /**
     * ФИО пользователя
     */
    lateinit var fio: String
    /**
     * Id текущей секции пользователя
     */
    var currentSectionId: String = ""
    /**
     * Наименование текущей секцияи пользователя
     */
    var currentSectionName: String = ""
}