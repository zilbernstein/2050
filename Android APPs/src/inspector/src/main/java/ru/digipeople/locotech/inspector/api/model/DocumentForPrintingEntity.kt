package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Документ для печати
 *
 * @author Aleksandr Brazhkin
 */
class DocumentForPrintingEntity {
    /**
     * Id документа
     */
    @SerializedName("id_document")
    var id: String = ""
    /**
     * Наименование документа
     */
    @SerializedName("document_name")
    var name = ""
}