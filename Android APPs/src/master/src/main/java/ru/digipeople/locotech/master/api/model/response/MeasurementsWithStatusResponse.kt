package ru.digipeople.locotech.master.api.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.MeasurementsEntity

/**
 * Модель замера  со статусом
 *
 * @author Sostavkin Grisha
 */
class MeasurementsWithStatusResponse {
    /**
     * Список аппаратных замеров
     */
    @SerializedName("measurements")
    var measurements: List<MeasurementsEntity> = emptyList()
    /**
     * Статус замеров
     */
    @SerializedName("hw_measurements_status")
    var measurementsStatus: String = ""
}