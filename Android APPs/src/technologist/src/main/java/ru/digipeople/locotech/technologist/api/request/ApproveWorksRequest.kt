package ru.digipeople.locotech.technologist.api.request

import com.google.gson.annotations.SerializedName

/**
 * Модель подтверждения работ
 */
class ApproveWorksRequest {
    /**
     * id
     */
    @SerializedName("id_remark")
    var remarkId = ""
    /**
     * список согласованных работ
     */
    @SerializedName("works_approved")
    var workIds = emptyList<String>()
}