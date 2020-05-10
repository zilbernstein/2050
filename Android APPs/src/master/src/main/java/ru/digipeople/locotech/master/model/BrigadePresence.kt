package ru.digipeople.locotech.master.model
/**
 * Модель явки бригады
 */
class BrigadePresence {
    /*
     * UUID бригады
     */
    var id: String = ""
    /*
     * Наименование бригады
     */
    var name: String = ""
    /*
     * Список явок сотрудников в бригаде
     */
    var workerPresenceList: List<WorkerPresence> = emptyList()

}