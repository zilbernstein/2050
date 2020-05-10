package ru.digipeople.locotech.worker.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель работ
 *
 * @author Kashonkov Nikita
 */
class WorkEntity {
    /**
     * Id работы
     */
    @SerializedName("id_work")
    lateinit var workId: String
    /**
     * Название работы
     */
    @SerializedName("work_name")
    lateinit var workName: String
    /**
     * Статус работы
     */
    @SerializedName("work_status")
    var workStatus: Int = 0
    /**
     * Количество повторов
     */
    @SerializedName("repeats")
    var repeats: Int = 0

    /**
     * Исполнители
     */
    @SerializedName("workers")
    var workers = mutableListOf<PerformerEntity>()
}