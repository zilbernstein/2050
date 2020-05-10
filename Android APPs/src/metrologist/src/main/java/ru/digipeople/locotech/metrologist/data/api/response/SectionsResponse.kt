package ru.digipeople.locotech.metrologist.data.api.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.metrologist.data.api.entity.RepairTypeEntity
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Модель ответа метода sections МП Мониторинг КП
 *
 * @author Michael Solenov
 */
class SectionsResponse : BaseCollectionResponse<SectionsResponse.Loco>() {

    class Loco {
        /**
         * Id локомотива
         */
        @SerializedName("loco_id")
        var id = ""
        /**
         * Номер локомотива
         */
        @SerializedName("loco_number")
        var number = ""
        /**
         * Субомер локомотива
         */
        @SerializedName("loco_subnumber")
        var subNumber = ""
        /**
         * Бортовой номер
         */
        @SerializedName("loco_name")
        var name = ""
        /**
         * Список секций
         */
        @SerializedName("sections_list")
        var sections = emptyList<Section>()
    }

    class Section {
        /**
         * Id секции
         */
        @SerializedName("section_id")
        var id = ""
        /**
         * Номер секции
         */
        @SerializedName("section_number")
        var number = ""
        /**
         * Подномер секции
         */
        @SerializedName("section_subnumber")
        var subNumber = ""
        /**
         * Наименование секции
         */
        @SerializedName("section_name")
        var name = ""
        /**
         * Флаг выбранности секции
         */
        @SerializedName("is_selected_section")
        var isSelected = false

        /**
         *
         */
        @SerializedName("repaire_type")
        var repairType: RepairTypeEntity? = null

        /**
         *
         */
        var state: String = ""
    }
}

