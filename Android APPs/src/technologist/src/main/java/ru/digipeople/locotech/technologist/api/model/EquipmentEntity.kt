package ru.digipeople.locotech.technologist.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель оборудования
 *
 * @author Sostavkin Grisha
 */
class EquipmentEntity {
    /**
     * Название локомотива
     */
    @SerializedName("equipement_name")
    var name: String = ""
    /**
     * Список секций
     */
    @SerializedName("section_list")
    var section: List<SectionEntity> = emptyList()
}