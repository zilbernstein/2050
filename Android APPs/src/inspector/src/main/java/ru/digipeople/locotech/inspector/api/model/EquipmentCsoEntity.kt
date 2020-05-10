package ru.digipeople.locotech.inspector.api.model

import com.google.gson.annotations.SerializedName

/**
 * Модель Оборудования Контрольно-Сервисной операции
 *
 * @author Kashonkov Nikita
 */
class EquipmentCsoEntity {
    /**
     * Название
     */
    @SerializedName("equipement_name")
    var title = ""

    /**
     * Список КСО
     */
    @SerializedName("cso_list")
    lateinit var csoList: List<ControlServiceOperationEntity>
}