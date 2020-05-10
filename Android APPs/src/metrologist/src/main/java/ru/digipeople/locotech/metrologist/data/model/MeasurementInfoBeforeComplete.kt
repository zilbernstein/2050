package ru.digipeople.locotech.metrologist.data.model

/**
 * Модель подтверждения замера
 * @author Michael Solenov
 */
class MeasurementInfoBeforeComplete {
    /**
     * Замер
     */
    lateinit var measurement: Measurement
    /**
     * Колесные пары
     */
    lateinit var wheelPairs: List<WheelPairShort>
    /**
     * Потенциальные замечания
     */
    lateinit var remarks: List<Remark>
}