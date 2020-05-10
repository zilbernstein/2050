package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель контрольной точки
 *
 * @author Sostavkin Grisha
 */
class ControlPointEntity {
    /**
     * Id контрольной точки
     */
    @SerializedName("id_checkpoint")
    lateinit var id: String
    /**
     * Название контрольной точки
     */
    @SerializedName("checkpoint_name")
    lateinit var name: String
    /**
     * Показатель контрольной точки
     */
    @SerializedName("checkpoint_gauge")
    lateinit var mark: String
}