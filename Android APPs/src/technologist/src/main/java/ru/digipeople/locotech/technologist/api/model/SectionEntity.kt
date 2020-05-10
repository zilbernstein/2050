package ru.digipeople.locotech.technologist.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель секции
 *
 * @author Sostavkin Grisha
 */
class SectionEntity {
    /**
     * Название/номер секции
     */
    @SerializedName("section_name")
    var id: String = ""
    /**
     * Список замечаний
     */
    @SerializedName("remark_list")
    lateinit var remarkList: List<RemarkEntity>
}