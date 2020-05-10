package ru.digipeople.locotech.inspector.api.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.inspector.api.model.RemarkEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Класс RemarkRzdResponse - модель ответа метода remarks_rzd МП Выпуск на линию
 * @author Kashonkov Nikita
 */
class RemarkRzdResponse: BaseCollectionResponse<RemarkEntity>(){
    /**
     * Название оборудования
     */
    @SerializedName("workshop_equipment_name")
    lateinit var equipmentTitle: String
    /**
     * Список групп
     */
    @SerializedName("remarks_rzd_list")
    lateinit var remarkList: List<RemarkEntity>
}