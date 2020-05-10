package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель Оборудования (Локомотива)
 *
 * @author Kashonkov Nikita
 */
class EquipmentEntity(
        /**
         * Id оборудования
         */
        @SerializedName("id_equipement")
        val id: String,

        /**
         * Номер оборудования
         */
        @SerializedName("equipement_number")
        val number: String,

        /**
         * Номер оборудования
         */
        @SerializedName("equipement_subnumber")
        val subNumber: String,

        /**
         * Бортовой номер
         */
        @SerializedName("equipement_name")
        val name: String,

        /**
         * Тип оборудования
         */
        @SerializedName("equipement_type")
        val type: String,

        /**
         * Время до окончания работ
         */
        @SerializedName("time_required")
        val timeRequired: Long = 0L,

        /**
         * Время нормативное
         */
        @SerializedName("time_left")
        val timeLeft: Long = 0L,

        /**
         * Прогресс выполнения
         */
        @SerializedName("complete_percent")
        val progress: Int = 0,

        /**
         * Флаг выбранности оборудования
         */
        @SerializedName("is_selected_equiment")
        val isSelected: Boolean = false,

        /**
         * Список секций
         */
        @SerializedName("sections_list")
        val sectionList: List<SectionEntity>
)