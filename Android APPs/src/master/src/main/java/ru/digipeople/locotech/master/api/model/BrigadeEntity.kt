package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель бригады
 *
 * @author Sostavkin Grisha
 */
class BrigadeEntity {
    /**
     * Id бригады
     */
    @SerializedName("brigade_id")
    lateinit var id: String
    /**
     * Название брнигады
     */
    @SerializedName("brigade_name")
    lateinit var brigadeName: String
    /**
     * Список сотрудников бригады
     */
    @SerializedName("worker_list")
    lateinit var brigadeWorkerList: List<WorkerForBrigadeEntity>
}