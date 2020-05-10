package ru.digipeople.locotech.master.data.work

import ru.digipeople.locotech.core.data.model.Performer
import java.util.*

/**
 * Модель - работа
 *
 * @author Kashonkov Nikita
 */
data class Work(
        /**
         * id
         */
        val id: Int,
        /**
         * Наименование работ
         */
        val title: String,
        /**
         * Исполнитель работ
         */
        var perfomer: MutableList<Performer>,
        /**
         * Показатель срочности
         */
        val urgency: WorkUrgence,
        /**
         * Комментарии
         */
        val comment: String,
        /**
         * Статус
         */
        var stat: WorkStatus,
        /**
         * Флаг согласования работ
         */
        var approved: WorkApprovedStatus,
        /**
         * Флаг того, что работы окончены
         */
        val finished: Boolean,
        /**
         * Наименование оборудования
         */
        val equimpent: String,
        /**
         * Время окончания работ
         */
        var endTime: Long,
        /**
         * Время начала работ
         */
        var startTime: Long

) {
    /**
     * Прогресс
     */
    var progress: Double = 0.0

    /**
     * Продолжительность работ
     */
    var length: Long = 0L

    init {
        if (startTime == 0L) {
            progress = 0.0
        } else {
            val time = Date().time
            val rawRemainingTime = endTime - time

            var workTime = endTime - startTime
            var goneTime = workTime - rawRemainingTime

            if (goneTime <= 0L) {
                progress = 1.0
            } else {
                progress = (goneTime.toFloat() / workTime.toFloat()).toDouble()
            }
        }

        length = endTime - startTime
    }


    override fun equals(other: Any?): Boolean {
        if (other is Work) {
            if (this.id == other.id) {
                return true
            }
        }
        return false
    }
}
