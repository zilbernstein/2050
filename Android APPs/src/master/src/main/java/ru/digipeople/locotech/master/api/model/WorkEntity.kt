package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель работы
 *
 * @author Kashonkov Nikita
 */
class WorkEntity {
    /**
     * Идентификатор работы
     */
    @SerializedName("id_work")
    lateinit var id: String
    /**
     * Наименование работы
     */
    @SerializedName("work_name")
    lateinit var title: String
    /**
     * Статус работы
     */
    @SerializedName("work_status")
    var status: Int = 0
    /**
     * Нормативное время
     */
    @SerializedName("time_limit")
    var normalTime = 0L
    /**
     * Оставшееся время
     */
    @SerializedName("time_remain")
    var remainTime = 0L
    /**
     * Исполнители
     */
    @SerializedName("worker_list")
    var performers = mutableListOf<PerformerEntity>()
    /**
     * Комментарий
     */
    @SerializedName("comment")
    var comment: CommentEntity? = null
    /**
     * Id причины остановки
     */
    @SerializedName("id_reason")
    var stopReasonId: String = ""
    /**
     * Флаг необходимости фотографий
     */
    @SerializedName("photo_required")
    var isPhotoRequired = false
    /**
     * Количество фотографий
     */
    @SerializedName("photo_count")
    var photoCount = 0
    /**
     * Название оборудование
     */
    @SerializedName("equipment_namenumber")
    var equipmentName: String = ""
    /**
     * Номер наряда
     */
    @SerializedName("order_number")
    var outfitNumber: String? = ""
    /**
     * Процент от первоначальной работы
     */
    @SerializedName("work_part_percent")
    var workPartPercent: Int = 100
    /**
     * Количество повторов
     */
    @SerializedName("repeats")
    var repeats = 0
    /**
     * Замеры
     */
    @SerializedName("measurements")
    var measurements: String? = null

    @SerializedName("has_tmc")
    var hasTmc = false

    @SerializedName("has_measurements")
    var hasMeasurements = false

    @SerializedName("has_mpi")
    var hasMpi = false
}