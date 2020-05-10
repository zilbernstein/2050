package ru.digipeople.locotech.technologist.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.CommentEntity

/**
 * Модель деталей работы
 *
 * @author Sostavkin Grisha
 */
class WorkDetailEntity {
    /**
     * Комментарий
     */
    @SerializedName("comment")
    lateinit var comment: CommentEntity
    /**
     * Выбрана работа ли для согласования
     */
    @SerializedName("checked_technologist")
    var isWorkApprove = false
    /**
     * Список параметров
     */
    @SerializedName("parameters_list")
    var parametersList: List<ParameterEntity> = listOf()
}