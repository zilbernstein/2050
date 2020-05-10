package ru.digipeople.locotech.master.data.groupassignment

/**
 * Группа работ
 *
 * @author Sostavkin Grisha
 */
data class WorkGroup(
        /**
         * id группы работ
         */
        var id: String,
        /**
         * Название группы работ
         */
        var workGroupName: String,
        /**
         * Список работ в группе
         */
        var workListEntity: List<WorkForWorkerAssignment>,
        /**
         * флаг выбранности
         */
        var isSelected: Boolean = false
)