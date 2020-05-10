package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель секции
 *
 * @author Kashonkov Nikita
 */
class SectionEntity(
        /**
         * Id секции
         */
        @SerializedName("id_section")
        val id: String,

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
        var equipmentHealth: List<EquipmentHealthEntity>,

        /**
         * Тип ремонта
         */
        @SerializedName("repaire_type")
        val repairType: RepairTypeEntity,

        /**
         * Состояние оборудования
         */
        @SerializedName("state")
        val state: String?,

        /**
         * Список с числом работ по вкладкам
         */
        @SerializedName("works_count")
        val worksCount: List<WorksCountEntity>?
)