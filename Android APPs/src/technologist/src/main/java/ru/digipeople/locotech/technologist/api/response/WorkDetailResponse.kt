package ru.digipeople.locotech.technologist.api.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.model.Comment
import ru.digipeople.locotech.technologist.api.model.ParameterEntity

/**
 * Ответ с сервера деталей работы
 *
 * @author Sostavkin Grisha
 */
class WorkDetailResponse{
    /**
     * Комментарий
     */
    @SerializedName("comment")
    lateinit var comment: Comment
    /**
     * Выбрана работа ли для согласования
     */
    @SerializedName("checked_technologist")
    var isWorkApprove = false
    /**
     * Список параметров
     */
    @SerializedName("parameters_list")
    lateinit var parametersList: List<ParameterEntity>
}