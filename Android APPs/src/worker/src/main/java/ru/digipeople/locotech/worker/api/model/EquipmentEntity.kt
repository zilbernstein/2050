package ru.digipeople.locotech.worker.api.model

import com.google.gson.annotations.SerializedName

/**
 * Класс [EquipmentEntity] - модель api обородувания
 *
 * @author Kashonkov Nikita
 */
class EquipmentEntity {
    /**
     * Id оборудования
     */
    @SerializedName("id_equipment")
    lateinit var equipmentId: String
    /**
     * Отображаесое название
     */
    @SerializedName("equip_sect_name")
    lateinit var equipmentName: String
    /**
     * Процент выполнения работ
     */
    @SerializedName("equip_percent")
    var equipmentProgress: Int = 0
    /**
     * Список сеций
     */
    @SerializedName("sections")
    var sectionList: List<SectionEntity> = emptyList()
}