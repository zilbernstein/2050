package ru.digipeople.locotech.master.model

/**
 * Группа работ
 *
 * @author Sostavkin Grisha
 */
class WorkGroup {
    /**
     * id группы работ
     */
    lateinit var id: String
    /**
     * Название группы работ
     */
    lateinit var workGroupName: String
    /**
     * Список работ в группе
     */
    lateinit var workListEntity: List<WorkForWorkerAssignment>
    /**
     * флаг выбранности
     */
    var isSelected: Boolean = false
}