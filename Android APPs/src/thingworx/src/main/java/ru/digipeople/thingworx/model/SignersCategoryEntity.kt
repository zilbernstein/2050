package ru.digipeople.thingworx.model

import com.google.gson.annotations.SerializedName

/**
 * Модель категории подписантов
 */
class SignersCategoryEntity {
    /**
     * 	ID категории подписантов
     */
    @SerializedName("category_id")
    var id = ""
    /**
     * 	Список подписантов в категории
     */
    @SerializedName("signer_list")
    var signers = emptyList<String>()
}