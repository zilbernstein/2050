package ru.digipeople.locotech.master.model

/**
 * Работа для назначения работников
 *
 * @author Sostavkin Grisha
 */
class WorkForWorkerAssignment {
    /**
     * id работы
     */
    lateinit var id: String
    /**
     * Наименование работы
     */
    lateinit var workName: String
    /**
     * Нормативное время
     */
    var timeLimit = 0L
    /**
     * Оставшееся время
     */
    var timeRemainL = 0L
    /**
     * Процент остатка наряда
     */
    var workPercent: Int = 0
    /**
     * Время остатка наряда
     */
    var workPartTime: Int = 0
    /**
     * Максимальное число исполнителей
     */
    var workerLimit: Int = 0
    /**
     * Номер заказа-наряда
     */
    lateinit var orderNumber: String
    /**
     * Количество повторов
     */
    var repeat: Int = 0
    /**
     * Флаг выбрана-ли работа
     */
    var isSelected: Boolean = false
}