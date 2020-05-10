package ru.digipeople.locotech.metrologist.data.model
/**
 * Модель категории замеров
 */
class MeasurementCategory {
    /**
     * Идентификатор категории
     */
    lateinit var id: String
    /**
     * Наименование категории
     */
    lateinit var name: String
    /**
     * Номер для сортировки
     */
    lateinit var order: String
    /**
     * Наличие замера
     */
    var hasMeasurement = false
}