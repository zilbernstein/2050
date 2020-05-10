package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель Должностное лицо (подписант)
 *
 * @author Mike Solenov
 */
class SignerEntity {
    /**
     * Id должностного лица
     */
    @SerializedName("id_executive")
    var id: String = ""
    /**
     * ФИО
     */
    @SerializedName("executive_name")
    var fio: String = ""
    /**
     * Должность
     */
   @SerializedName("executive_duty")
    var post: String = ""
}
