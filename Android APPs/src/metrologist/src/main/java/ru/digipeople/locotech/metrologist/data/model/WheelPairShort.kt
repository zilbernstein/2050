package ru.digipeople.locotech.metrologist.data.model

/**
 * Модель колесной пары (короткая)
 *
 * @author Michael Solenov
 */
class WheelPairShort {
    /**
     * идентификатор КП
     */
    lateinit var pairId: String
    /**
     * номер КП
     */
    lateinit var pairNumber: String
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
    lateinit var flangeRigthNumber: String
    /**
     * признак необходимости обточки
     */
    var mustRepair = false
    /**
     * идентификатор причины обточки
     */
    lateinit var repairReasonId: String
    /**
     * наименование причины обточки
     */
    lateinit var repairReasonName: String
}