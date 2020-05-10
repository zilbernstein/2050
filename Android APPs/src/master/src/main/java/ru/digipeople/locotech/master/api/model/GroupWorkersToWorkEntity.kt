package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель группы исполнителей для работы
 * @author Sostavkin Grisha
 */
class GroupWorkersToWorkEntity {
    /**
     * id работы
     */
    @SerializedName("work_id")
    lateinit var id: String
    /**
     * Список исполнителей
     */
    @SerializedName("workers_list")
    lateinit var workersList: Collection<WorkerToWorkEntity>
}