package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Модель оборудования
 *
 * @author Michael Solenov
 */
class EquipmentEntity {
    /**
     * Id оборудования
     */
    @SerializedName("id_equipement")
    var id: String = ""
    /**
     * Номер оборудования
     */
    @SerializedName("equipement_number")
    var number = ""
    /**
     * Номер оборудования
     */
    @SerializedName("equipement_subnumber")
    var subNumber = ""
    /**
     * Бортовой номер
     */
    @SerializedName("equipement_name")
    var name = ""
    /**
     * Тип оборудования
     */
    @SerializedName("equipement_type")
    var type = ""
    /**
     * Время до окончания работ
     */
    @SerializedName("time_required")
    var timeRequired = 0L
    /**
     * Время нормативное
     */
    @SerializedName("time_left")
    var timeLeft = 0L
    /**
     * Прогресс выполнения
     */
    @SerializedName("complete_percent")
    var progress: Int = 0
    /**
     * Флаг выбранности оборудования
     */
    @SerializedName("is_selected_equiment")
    var isSelected: Boolean = false
    /**
     * Список секций
     */
    @SerializedName("sections_list")
    var sectionList: List<SectionEntity> = mutableListOf()
}