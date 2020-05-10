package ru.digipeople.locotech.master.api.model

/**
 * Модель для передачи на сервер двух коллекций
 *
 * @author Sostavkin Grisha
 */
data class GroupAssignmentIdListEntity(val workList: Collection<String>, val workerList: Collection<String>)