package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель исполнителя для работы
 *
 * @author Sostavkin Grisha
 */
class WorkerToWorkEntity {
    /**
     * id исполнителя
     */
    @SerializedName("worker_id")
    lateinit var id: String
}