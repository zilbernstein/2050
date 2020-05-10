package ru.digipeople.locotech.master.model
/**
 * Статусы получения аппаратного замера
 */
enum class MeasurementStatus {
    /*
     * Задача не поставлена
     */
    NO_TASK,
    /*
     * В ожидании замеров
     */
    WAITING,
    /*
     * Замеры получены
     */
    RECEIVED
}