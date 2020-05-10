package ru.digipeople.locotech.inspector.api.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.inspector.api.model.RemarkEntity

/**
 * Класс RemarkSldResponse - модель ответа метода remarks_sld МП Выпуск на линию
 * @author Kashonkov Nikita
 */
class RemarkSldResponse{
    /**
     * Название оборудования
     */
    @SerializedName("workshop_equipment_name")
    lateinit var equipmentTitle: String
    /**
     * Список групп
     */
    @SerializedName("remarks_sld_list")
    lateinit var remarkList: List<RemarkEntity>
}