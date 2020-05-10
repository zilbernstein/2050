package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
/**
 * Модель бригады явки сотрудников
 */
class BrigadePresenceEntity(
        /*
        * UUID бригады
        */
        @SerializedName("brigade_id")
        val id: String,
        /*
        * наименование бригады
        */
        @SerializedName("brigade_name")
        val name: String,
        /*
        * список сотрудников
        */
        @SerializedName("worker_list")
        val workerPresenceList: List<WorkerPresenceEntity>
)