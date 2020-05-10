package ru.digipeople.locotech.master.model

/**
 * Модель списка исполнителей работы
 *
 * @author Sostavkin Grisha
 */
class GroupWorkersToWork (
    /**
     * id работы
     */
    var id: String,
    /**
     * Список исполнителей
     */
    var workersList: List<WorkerToWork>
)