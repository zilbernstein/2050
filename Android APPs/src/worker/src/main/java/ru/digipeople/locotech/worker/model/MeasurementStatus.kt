package ru.digipeople.locotech.worker.model

/**
 * enum для статуса замеров
 */
enum class MeasurementStatus {
    /**
     * нет задания
     */
    NO_TASK,
    /**
     * в ожидании замеров
     */
    WAITING,
    /**
     * получены аппаратнные замеры
     */
    RECEIVED
}