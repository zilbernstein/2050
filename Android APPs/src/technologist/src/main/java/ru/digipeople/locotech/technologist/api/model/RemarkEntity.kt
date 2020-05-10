package ru.digipeople.locotech.technologist.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity

/**
 * Модель замечания
 *
 * @author Sostavkin Grisha
 */
class RemarkEntity {
    /**
     * Id замечания
     */
    @SerializedName("id_remark")
    var id: String = ""
    /**
     * Название замечания
     */
    @SerializedName("remark_name")
    var name: String = ""
    /**
     * Комментарий к замечанию
     */
    @SerializedName("remark_comment")
    lateinit var comment: CommentEntity
    /**
     * Дата замечания
     */
    @SerializedName("remark_date")
    var date = 0L
    /**
     * Количество фотографий по замечанию
     */
    @SerializedName("photo_count")
    var photoAmount: Long = 0L
    /**
     * Работы по замечанию
     */
    @SerializedName("work_list")
    var workList: List<WorkEntity> = listOf()
    /**
     * Автор замечания
     */
    @SerializedName("remark_author")
    var author: String = ""
}