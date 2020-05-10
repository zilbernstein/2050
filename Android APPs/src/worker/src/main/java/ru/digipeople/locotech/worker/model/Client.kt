package ru.digipeople.locotech.worker.model

/**
 * Модель пользователя
 *
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
     * Название текущей работы
     */
    lateinit var workName: String
    /**
     * Id текущей работы
     */
    lateinit var workId: String
    /**
     * Флаг того что работник находится в смене
     */
    var isInShift = false
}