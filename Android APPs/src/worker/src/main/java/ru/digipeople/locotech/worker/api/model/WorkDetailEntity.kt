package ru.digipeople.locotech.worker.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель деталки работы
 *
 * @author Kashonkov Nikita
 */
class WorkDetailEntity {
    /**
     * Название работы
     */
    @SerializedName("work_name")
    lateinit var workName: String
    /**
     * Статус работы
     */
    @SerializedName("work_status")
    var workStatus = 0
    /**
     * Время для выполнения работы
     */
    @SerializedName("time_limit")
    var timeLimit = 0L
    /**
     * Прошедшее время
     */
    @SerializedName("time_remain")
    var timeRemain = 0L
    /**
     * Название оборудования
     */
    @SerializedName("equipement_name")
    lateinit var equipmentName: String
    /**
     * Номер оборудования
     */
    @SerializedName("equipement_number")
    lateinit var equipmentNumber: String
    /**
     * Прогресс по оборудованию
     */
    @SerializedName("equipement_progres")
    var equipmentProgress: Int = 0
    /**
     * Список ТМЦ
     */
    @SerializedName("cam_list")
    var camList: List<TMCInWorkEntity> = emptyList()
    /**
     * Комментарий
     */
    @SerializedName("comment")
    var comment: CommentEntity? = null
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
    /**
     * Статус получения аппаратных замеров
     */
    @SerializedName("hw_measurements_status")
    var measurementStatus: String = ""
}