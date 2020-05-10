package ru.digipeople.locotech.master.data.groupassignment

import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель работника для расчета нагрузки
 *
 * @author Sostavkin Grisha
 */
data class WorkerForBrigade(
        /**
         * Исполнитель
         */
        var performer: Performer,
        /**
         * Признак выбранного
         */
        var isChecked: Boolean = false,
        /**
         * Процент загруженности
         */
        var workLoad: Int = 0,
        /**
         * Продолжительность смены
         */
        var shiftDuration: Float = 0F,
        /**
         * Доступен для назначения
         */
        var isAccessible: Boolean = false,
        /**
         * Процент загрузки
         */
        var newLoadPercent: Float = 0f
){
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as WorkerForBrigade

                if (performer != other.performer) return false
                if (isChecked != other.isChecked) return false
                if (workLoad != other.workLoad) return false
                if (shiftDuration != other.shiftDuration) return false
                if (isAccessible != other.isAccessible) return false

                return true
        }

        override fun hashCode(): Int {
                var result = performer.hashCode()
                result = 31 * result + isChecked.hashCode()
                result = 31 * result + workLoad
                result = 31 * result + shiftDuration.hashCode()
                result = 31 * result + isAccessible.hashCode()
                return result
        }
}