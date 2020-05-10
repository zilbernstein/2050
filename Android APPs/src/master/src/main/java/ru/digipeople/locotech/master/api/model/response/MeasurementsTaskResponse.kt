package ru.digipeople.locotech.master.api.model.response

import com.google.gson.annotations.SerializedName
/**
 * Модель задачи на получение замера
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