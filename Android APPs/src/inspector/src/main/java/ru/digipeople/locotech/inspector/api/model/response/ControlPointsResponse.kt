package ru.digipeople.locotech.inspector.api.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.inspector.api.model.ControlPointEntity

/**
 * Класс ControlPointsResponse - модель ответа метода work_checkpoints МП Выпуск на линию
 * @author Sostavkin Grisha
 */
class ControlPointsResponse {
    /**
     * Название работы
     */
    @SerializedName("work_name")
    lateinit var workName: String
    /**
     * Список контрольных точек
     */
    @SerializedName("checkpoints_list")
    lateinit var checkPointsList: List<ControlPointEntity>
}