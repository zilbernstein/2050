package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.Stage
/**
 * Модель ответа метода создания задания на получения замера
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