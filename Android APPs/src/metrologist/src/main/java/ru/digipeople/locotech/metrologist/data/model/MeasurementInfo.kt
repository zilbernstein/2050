package ru.digipeople.locotech.metrologist.data.model

/**
 * Модель информации о замере
 *
 * @author Michael Solenov
 */
class MeasurementInfo {
    /**
     * Замер
     */
    lateinit var measurement: Measurement
    /**
     * Основные показатели по замеру
     */
    lateinit var parameters: List<Parameter<*>>
}