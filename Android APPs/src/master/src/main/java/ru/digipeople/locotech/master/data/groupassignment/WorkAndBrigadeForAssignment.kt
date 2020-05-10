package ru.digipeople.locotech.master.data.groupassignment

/**
 * Модель списка работ без исполнителей и списка бригад
 *
 * @author Sostavkin Grisha
 */
data class WorkAndBrigadeForAssignment (
        /**
     * Список групп работ
     */
    var workGroupList: List<WorkGroup>,
        /**
     * Список бригад
     */
    var brigadeList: List<Brigade>
)