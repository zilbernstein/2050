package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity

/**
 * Модель работы
 *
 * @author Kashonkov Nikita
 */
class WorkEntity {
    /**
     * Id работы
     */
    @SerializedName("id_work")
    lateinit var id: String
    /**
     * Количество фотографий
     */
    @SerializedName("photos_count")
    var photosCount = 0
    /**
     * Название работы
     */
    @SerializedName("work_name")
    lateinit var title: String
    /**
     * Флаг наличия контрольных точек
     */
    @SerializedName("hasCheckpoints")
    var hasCheckpoints = false
    /**
     * Статус
     */
    @SerializedName("work_status")
    var status = 0
    /**
     * Комментарий
     */
    @SerializedName("work_comment")
    lateinit var comment: CommentEntity
}