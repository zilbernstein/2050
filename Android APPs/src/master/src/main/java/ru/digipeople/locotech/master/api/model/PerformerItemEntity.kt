package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель исполнителя с флагом в работе
 *
 * @author Kashonkov Nikita
 */
class PerformerItemEntity {
    /**
     * Исполнитель
     */
    @SerializedName("worker")
    lateinit var performer: PerformerEntity
    /**
     * Флаг того что в работе
     */
    @SerializedName("isChecked")
    var isInWork = false
    /**
     * Процент загрузки
     */
    @SerializedName("workload_percent")
    var loadPercent = 0.0
    /**
     * Длина смены
     */
    @SerializedName("shift_duration")
    var shiftDuration = 0.0
}