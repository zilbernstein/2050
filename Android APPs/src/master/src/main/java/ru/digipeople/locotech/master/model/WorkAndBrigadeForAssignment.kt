package ru.digipeople.locotech.master.model

/**
 * Модель списка работ без исполнителей и списка бригад
 *
 * @author Sostavkin Grisha
 */
class WorkAndBrigadeForAssignment {
    /**
     * Список групп работ
     */
    lateinit var workGroupList: List<WorkGroup>
    /**
     * Список бригад
     */
    lateinit var brigadeList: List<Brigade>
    /**
     * Ограничение на максимальное количество сотрудников
     */
    var workerLimit: Int = 0
}