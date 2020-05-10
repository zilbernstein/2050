package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель замечания
 *
 * @author Kashonkov Nikita
 */
class RemarkEntity {
    /**
     * Id замечания
     */
    @SerializedName("id_remark")
    lateinit var id: String
    /**
     * Наименование замечания
     */
    @SerializedName("remark_name")
    lateinit var title: String
    /**
     * Статус замечания
     */
    @SerializedName("remark_status")
    var status: Int = 0
    /**
     * Комментарий к замечанию
     */
    @SerializedName("remark_comment")
    var comment: CommentEntity? = null
    /**
     * Процент выполнения по локомтиву или ЛО
     */
    @SerializedName("equipment_complete_perc")
    var equipmentPercent = 0
    /**
     * Осталось времени на работу
     */
    @SerializedName("equipment_time_left")
    var equipmentTimeLeft = 0L
    /**
     * Все исполнители по замечанию
     */
    @SerializedName("worker_list")
    var performerList = mutableListOf<PerformerEntity>()
    /**
     * Количетво фотографий
     */
    @SerializedName("photo_count")
    var photoCount = 0
    /**
     * Возможность добавление работ
     */
    @SerializedName("enable_add_work")
    var isAddWorkEnable = false
    /**
     * Сисок работ в замечании
     */
    @SerializedName("work_list")
    lateinit var workList: List<WorkEntity>
    /**
     * Автор замечания
     */
    @SerializedName("remark_author")
    var author: String = ""
}