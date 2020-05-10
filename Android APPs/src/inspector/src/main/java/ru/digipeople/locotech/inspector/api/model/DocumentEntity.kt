package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель документа для книги ремонтов
 * @author Kashonkov Nikita
 */
class DocumentEntity {
    /**
     * 	ID документа
     */
    @SerializedName("id_document")
    var id: String = ""
    /**
     * Название документа
     */
    @SerializedName("document_name")
    var name: String = ""
    /**
     * URL на документ
     */
    @SerializedName("document_url")
    var url: String = ""
}