package ru.digipeople.locotech.core.data.api.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.thingworx.model.base.BaseCollectionResponse

/**
 * Модель ответа на метод выбора секции
 */
class SelectSectionResponse : BaseCollectionResponse<SelectSectionResponse.Equipment>() {
    class Equipment {
        /**
         * UUID выбранного оборудования (секции)
         */
        @SerializedName("id")
        var id = ""
        /**
         * Наименование оборудования (секции)
         */
        @SerializedName("name")
        var name = ""
        /**
         * Тип оборудования
         */
        @SerializedName("type")
        var type = ""
    }
}