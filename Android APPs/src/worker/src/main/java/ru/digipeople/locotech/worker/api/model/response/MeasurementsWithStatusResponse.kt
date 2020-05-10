package ru.digipeople.locotech.worker.api.model.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.MeasurementsEntity

/**
 * Класс [MeasurementsWithStatusResponse] - модель ответа метода check_hw_measurements_ready МП Сотрудник
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