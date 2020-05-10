package ru.digipeople.locotech.technologist.api.request

import com.google.gson.annotations.SerializedName

/**
 * Модель отклонения работ
 */
class RejectWorksRequest {
    /**
     * id
     */
    @SerializedName("id_remark")
    var remarkId = ""
    /**
     * Список отклоненных работ
     */
    @SerializedName("works_declined")
    var workIds = emptyList<String>()
}