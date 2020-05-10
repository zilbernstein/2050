package ru.digipeople.locotech.worker.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель секции
 *
 * @author Kashonkov Nikita
 */
class SectionEntity {
    /**
     * Id секции
     */
    @SerializedName("id_section")
    lateinit var sectionId: String
    /**
     * Название секции
     */
    @SerializedName("section_name")
    lateinit var sectionName: String
    /**
     * Список работ
     */
    @SerializedName("works")
    var workList: List<WorkEntity> = emptyList()
}