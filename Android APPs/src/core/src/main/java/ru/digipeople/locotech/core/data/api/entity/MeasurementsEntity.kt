package ru.digipeople.locotech.core.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Модель замера
 *
 * @author Sostavkin Grisha
 */
class MeasurementsEntity {
    /**
     * Id показателяя замера
     */
    @SerializedName("measurement_id")
    lateinit var measurementId: String
    /**
     * Название показателя замера
     */
    @SerializedName("measurement_name")
    lateinit var measurementName: String
    /**
     * Идентификатор характеристики показателя
     */
    @SerializedName("characteristic_id")
    lateinit var characteristicId: String
    /**
     * Название характеристики показателя
     */
    @SerializedName("characteristic_name")
    lateinit var characteristicName: String
    /**
     * Этап замера
     */
    @SerializedName("measurement_stage")
    var stage: Int = -1
    /**
     * Признак выполнения замера
     */
    @SerializedName("measurement_complete")
    var isMeasurementComplete: Boolean = false
    /**
     * Норма
     */
    @SerializedName("measurement_norm")
    lateinit var measurementNorm: String
    /**
     * Значение
     */
    @SerializedName("measurement_value")
    lateinit var measurementValue: String
    /**
     * Тип данных для вводимых значений
     */
    @SerializedName("value_type")
    lateinit var valueType: String
    /**
     * Соответствие значение норме
     */
    @SerializedName("value_compliance")
    var valueCompliance: Boolean = false
    /**
     * Сотрудник
     */
    @SerializedName("worker")
    lateinit var worker: PerformerEntity
    /**
     * Дата измерения
     */
    @SerializedName("measurement_date")
    var measurementDate: Long = 0L
    /**
     * Комментарий
     */
    @SerializedName("measurement_comment")
    lateinit var measurementComment: CommentEntity
    /**
     * Идентификатор экземпляра работы
     */
    @SerializedName("work_id")
    lateinit var workId: String

    /**
     * "Тип замера", "Аппаратный", если - true и "Ручной" в случае если - false
     */
    @SerializedName("is_hw_measurement")
    var isHardware = false
}
