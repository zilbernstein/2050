package ru.digipeople.locotech.master.data.groupassignment

/**
 * Модель бригады
 *
 * @author Sostavkin Grisha
 */
data class Brigade(
        /**
         * Id бригады
         */
        var id: String,
        /**
         * Название брнигады
         */
        var brigadeName: String,
        /**
         * Список сотрудников бригады
         */
        var brigadeWorkerList: List<WorkerForBrigade>,
        /**
         * Флаг выбранности
         */
        var isSelected: Boolean = false
)