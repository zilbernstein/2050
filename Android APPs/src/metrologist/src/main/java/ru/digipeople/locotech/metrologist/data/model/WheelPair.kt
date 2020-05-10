package ru.digipeople.locotech.metrologist.data.model

/**
 * Модель колесной пары (полная)
 *
 * @author Michael Solenov
 */
class WheelPair {
    /**
     * идентификатор КП
     */
    lateinit var id: String
    /**
     * номер КП
     */
    lateinit var number: String
    /**
     * номер оси
     */
    lateinit var axisNumber: String
    /**
     * номер левого бандажа
     */
    lateinit var flangeLeftNumber: String
    /**
     * номер правого бандажа
     */
    lateinit var flangeRightNumber: String
    /**
     * признак необходимости обточки
     */
    var mustRepair: Boolean = false
    /**
     * идентификатор причины обточки
     */
    lateinit var repairReasonId: String
    /**
     * наименование причины обточки
     */
    lateinit var repairReasonName: String
    /**
     * параметры колёс
     */
    lateinit var wheelParams: List<WheelParam<*>>
}