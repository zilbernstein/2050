package ru.digipeople.locotech.worker.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Класс [MeasurementsTaskResponse] - модель ответа метода task_for_hw_measurements МП Сотрудник
 */
class MeasurementsTaskResponse {
    /**
     * Статус замеров
     */
    @SerializedName("hw_measurements_status")
    var measurementsStatus: String = ""
    /**
     * Список измерительных приборов для проведения измерения
     */
    @SerializedName("measuring_devices")
    var measurementsDevices: List<String> = emptyList()
    /**
     * ID задания на получение аппаратных замеров
     */
    @SerializedName("task_id")
    var taskId: String = ""
}