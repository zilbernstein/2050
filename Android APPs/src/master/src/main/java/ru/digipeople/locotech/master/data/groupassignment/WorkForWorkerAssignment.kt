package ru.digipeople.locotech.master.data.groupassignment

/**
 * Работа для назначения работников
 *
 * @author Sostavkin Grisha
 */
data class WorkForWorkerAssignment(
        /**
         * id работы
         */
        var id: String,
        /**
         * Наименование работы
         */
        var workName: String,
        /**
         * Нормативное время
         */
        var timeLimit: Long = 0L,
        /**
         * Оставшееся время
         */
        var timeRemainL: Long = 0L,
        /**
         * Процент остатка наряда
         */
        var workPercent: Int = 0,
        /**
         * Время остатка наряда
         */
        var workPartTime: Int = 0,
        /**
         * Максимальное число исполнителей
         */
        var workerLimit: Int = 0,
        /**
         * Номер заказа-наряда
         */
        var orderNumber: String,
        /**
         * Количество повторов
         */
        var repeat: Int = 0,
        /**
         * Флаг выбранности
         */
        var isSelected: Boolean = false
)