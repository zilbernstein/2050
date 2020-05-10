package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
/**
 * Модель шаблона группового назначения
 */
data class AssignmentTemplateEntity(
        /**
         * ID шаблона
         */
        @SerializedName("id_template")
        val id: String,
        /**
         * Название шаблона
         */
        @SerializedName("template_name")
        val name: String
)