package ru.digipeople.locotech.master.api.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.master.api.model.RemarkEntity

/**
 * Модель оборудования для замечаний
 *
 * @author Kashonkov Nikita
 */
class EquipmentRemarkResponse{
    /**
     * Требуемое время выполнение работ по оборудованию
     */
    @SerializedName("equipment_time_required")
    var equipmentTimeRequired = 0L
    /**
     * Прошедшее время
     */
    @SerializedName("equipment_time_left")
    var equipmentTimeLeft = 0L
    /**
     * Прцент выполнения по оборудованию
     */
    @SerializedName("equipment_complete_percent")
    var equipmentPercent = 0f
    /**
     * Список замечаний
     */
    @SerializedName("remark_list")
    var remarkList = listOf<RemarkEntity>()

}