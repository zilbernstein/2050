package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Категория подписантов для печати
 */
class DocumentSignerEntity {
    /**
     * Название категории подписантов
     */
    @SerializedName("category_title")
    var title = ""

    /**
     * Id категории подписантов
     */
    @SerializedName("category_id")
    var id: String = ""

    /**
     * Список подписантов в категории
     */
    @SerializedName("signer_list")
    var signers = mutableListOf<SignerList>()
}

/**
 * Подписанты для печати
 */
class SignerList{
    /**
     * id подписанта
     */
    @SerializedName("id_signer")
    var id = ""

    /**
     * имя подписанта
     */
    @SerializedName("signer_name")
    var name: String = ""
}