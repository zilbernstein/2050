package ru.digipeople.locotech.core.data.model

import java.util.*

/**
 * Модель замера
 *
 * @author Sostavkin Grisha
 */
class Measurement {
    /**
     * Id показателяя замера
     */
    lateinit var measurementId: String
    /**
     * Название показателя замера
     */
    lateinit var measurementName: String
    /**
     * Идентификатор характеристики показателя
     */
    lateinit var characteristicId: String
    /**
     * Название характеристики показателя
     */
    lateinit var characteristicName: String
    /**
     * Этап замера
     */
    lateinit var stage: Stage
    /**
     * Признак выполнения замера
     */
    var isMeasurementComplete: Boolean = false
    /**
     * Норма
     */
    lateinit var measurementNorm: String
    /**
     * Значение
     */
    lateinit var measurementValue: String
    /**
     * Тип данных для вводимых значений
     */
    lateinit var valueType: MeasureValueType
    /**
     * Соответствие значение норме
     */
    var valueCompliance: Boolean = false
    /**
     * Сотрудник
     */
    lateinit var worker: Performer
    /**
     * Дата измерения
     */
    var measurementDate: Date? = null
    /**
     * Комментарий
     */
    lateinit var measurementComment: Comment
    /**
     * Идентификатор экземпляра работы
     */
    lateinit var workId: String
    /**
     * Тип аппаратного замера
     */
    var isHardware = false
}