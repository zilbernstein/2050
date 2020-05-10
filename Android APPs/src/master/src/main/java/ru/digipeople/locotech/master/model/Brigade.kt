package ru.digipeople.locotech.master.model

/**
 * Модель бригады
 *
 * @author Sostavkin Grisha
 */
class Brigade {
    /**
     * Id бригады
     */
    lateinit var id: String
    /**
     * Название брнигады
     */
    lateinit var brigadeName: String
    /**
     * Список сотрудников бригады
     */
    lateinit var brigadeWorkerList: List<WorkerForBrigade>
    /**
     * Флаг выбранности
     */
    var isSelected: Boolean = false
}