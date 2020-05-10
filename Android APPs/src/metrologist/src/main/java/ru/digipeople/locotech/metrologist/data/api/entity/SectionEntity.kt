package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.metrologist.data.api.response.EquipmentHealthEntity

/**
 * Модель секции
 *
 * @author Michael Solenov
 */
class SectionEntity(
        /**
         * Id секции
         */
        @SerializedName("id_section")
        var id: String,

        /**
         * Номер секции
         */
        @SerializedName("section_number")
        var number: String,

        /**
         * Подномер секции
         */
        @SerializedName("section_subnumber")
        var subNumber: String,

        /**
         * Наименование секции
         */
        @SerializedName("section_name")
        var name: String,

        /**
         * Тип секции
         */
        @SerializedName("section_type")
        var type: String,

        /**
         * Флаг выбранности секции
         */
        @SerializedName("is_selected_section")
        var isSelected: Boolean = false,

        /**
         * Состояние секции
         */
        @SerializedName("equipment_health")
        var equipmentHealth: List<EquipmentHealthEntity>
)