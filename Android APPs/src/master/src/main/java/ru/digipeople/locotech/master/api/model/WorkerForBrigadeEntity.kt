package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель работника для расчета нагрузки
 *
 * @author Sostavkin Grisha
 */
class WorkerForBrigadeEntity {
    /**
     * Исполнитель
     */
    @SerializedName("Performer")
    lateinit var performer: PerformerEntity
    /**
     * Признак выбранного
     */
    @SerializedName("isChecked")
    var isChecked = false
    /**
     * Процент загруженности
     */
    @SerializedName("workload_percent")
    var workLoad: Int = 0
    /**
     * Продолжительность смены
     */
    @SerializedName("shift_duration")
    var shiftDuration = 0F
    /**
     * Доступен для назначения
     */
    @SerializedName("isAccessible")
    var isAccessible = false
}