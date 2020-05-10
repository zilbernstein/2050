package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель замечания из общего списка замечаний
 *
 * @author Kashonkov Nikita
 */
class RemarkFromCatalogEntity{
    /**
     * id Замечания в списке замечаний
     */
    @SerializedName("id_remark")
    lateinit var id: String
    /**
     * Наименование замечания
     */
    @SerializedName("remark_name")
    lateinit var title: String
}