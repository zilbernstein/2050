package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity

/**
 * Модель Контрольно-Сервисной операции
 *
 * @author Kashonkov Nikita
 */
class ControlServiceOperationEntity {
    /**
     * Id
     */
    @SerializedName("id_cso")
    var id = ""

    /**
     * Название
     */
    @SerializedName("cso_name")
    var title = ""

    /**
     * Статус
     */
    @SerializedName("cso_status")
    var status = 0

    /**
     * Комментарий
     */
    @SerializedName("cso_comment")
    lateinit var comment: CommentEntity

    /**
     * Количество фотографий
     */
    @SerializedName("photos_count")
    var photoAmount = 0

    /**
     * Показатель нормы
     */
    @SerializedName("rate_value")
    var rateValue = ""
}