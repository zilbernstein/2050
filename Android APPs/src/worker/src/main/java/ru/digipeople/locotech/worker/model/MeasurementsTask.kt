package ru.digipeople.locotech.worker.model

import ru.digipeople.locotech.core.data.model.Stage

/**
 * Модель замеров задачи
 */
class MeasurementsTask {
    /**
     * Этап замеров
     */
    var measurementsStage: Stage = Stage.BEFORE_REPAIR
    /**
     * Статус замеров
     */
    var measurementsStatus: MeasurementStatus = MeasurementStatus.RECEIVED
    /**
     * Список измерительных приборов для проведения измерения
     */
    var measurementsDevices: List<String> = emptyList()
    /**
     * ID задания на получение аппаратных замеров
     */
    var taskId: String = ""
}