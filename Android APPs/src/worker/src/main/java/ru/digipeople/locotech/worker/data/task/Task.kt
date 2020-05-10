package ru.digipeople.locotech.worker.data.task

import ru.digipeople.locotech.worker.data.reason.Reason
import ru.digipeople.locotech.worker.data.tmc.TMC

/**
 * Модель Задания
 *
 * @author Kashonkov Nikita
 */
data class Task(
        /**
         * id задания
         */
        val id: String,
        /**
         * id оборудования
         */
        val eqipmentId: String,
        /**
         * id секции
         */
        val sectionId: String,
        /**
         * Статус работы
         */
        var status: TaskStatus,
        /**
         * Время начала работ
         */
        var startTime: Long,
        /**
         * Текущий прогресс
         */
        var progress: Double,
        /**
         * Длительность
         */
        val duration: Long,
        /**
         * Товарно-материальные ценности
         */
        val tmcList: MutableList<TMC>

) {
    /**
     * Время начала работ
     */
    var endTime: Long
    /**
     * Причины остановки
     */
    var reasonList: MutableList<Reason>
    /**
     * Комментарии
     */
    var comment = "Текст комментария"

    init {
        endTime = startTime + duration
        reasonList = mutableListOf()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Task) return false
        if (this.id == other.id) {
            return true
        } else {
            return false
        }
    }
}