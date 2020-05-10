package ru.digipeople.locotech.inspector.api.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.inspector.api.model.CyclicGroupEntity

/**
 * Класс CyclicWorkResponse - модель ответа метода cyclic_works МП Выпуск на линию
 * @author Kashonkov Nikita
 */
class CyclicWorkResponse {
    /**
     * Название оборудования
     */
    @SerializedName("workshop_equipment_name")
    lateinit var equipmentTitle: String
    /**
     * Список групп
     */
    @SerializedName("group_works_list")
    lateinit var groupList: List<CyclicGroupEntity>
}