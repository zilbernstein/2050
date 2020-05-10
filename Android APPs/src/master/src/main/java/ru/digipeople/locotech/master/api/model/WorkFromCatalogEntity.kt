package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель работы из общего списка работ
 *
 * @author Kashonkov Nikita
 */
class WorkFromCatalogEntity {
    /**
     * id Замечания в списке замечаний
     */
    @SerializedName("id_work")
    lateinit var id: String
    /**
     * Наименование замечания
     */
    @SerializedName("work_name")
    lateinit var title: String
}