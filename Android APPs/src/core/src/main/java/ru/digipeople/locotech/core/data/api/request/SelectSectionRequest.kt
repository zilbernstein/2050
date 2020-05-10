package ru.digipeople.locotech.core.data.api.request

import com.google.gson.annotations.SerializedName

/**
 * Запрос на выбор секции
 */
class SelectSectionRequest(
        /**
         * UUID оборудования
         */
        @SerializedName("id_section")
        var sectionId: String = ""
)