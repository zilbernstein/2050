package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель состояния секции
 *
 * @author Kashonkov Nikita
 */
class EquipmentHealthEntity {
    /**
     * Статус
     */
    @SerializedName("id_status")
    lateinit var status: String
    /**
     * Тип
     */
    @SerializedName("id_parameter")
    lateinit var type: String
    /**
     * Позиция
     */
    @SerializedName("position")
    var position = 0
}