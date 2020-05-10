package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity

/**
 * Модель замечания
 *
 * @author Kashonkov Nikita
 */
class RemarkEntity {
    /**
     * Id
     */
    @SerializedName("id_group_works")
    var id = ""

    /**
     * Название
     */
    @SerializedName("remark_name")
    var title = ""

    /**
     * Автор замечания
     */
    @SerializedName("remark_creator")
    var author = ""

    /**
     * Дата создания замечания
     */
    @SerializedName("remark_date")
    var date = 0L

    /**
     * Список работ
     */
    @SerializedName("works_list")
    lateinit var works: List<WorkEntity>

    /**
     * Количество фотографий
     */
    @SerializedName("photos_count")
    var photoAmount = 0

    /**
     * Комментарий к замечанию
     */
    @SerializedName("remark_comment")
    lateinit var comment: CommentEntity

    /**
     * Количество работ в замечании
     */
    @SerializedName("work_count")
    var workCount = 0
}