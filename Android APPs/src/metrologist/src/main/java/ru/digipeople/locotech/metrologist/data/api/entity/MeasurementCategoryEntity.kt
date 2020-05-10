package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
/**
 * Модель категории замеров
 */
class MeasurementCategoryEntity {
    /**
     * идентификатор категории
     */
    @SerializedName("category_id")
    lateinit var id: String
    /**
     *название категории
     */
    @SerializedName("category_name")
    lateinit var name: String
    /**
     * номер для сортировки
     */
    @SerializedName("category_order")
    lateinit var order: String
    /**
     * наличие завершенного замера
     */
    @SerializedName("has_measurement")
    var hasMeasurement: Boolean = false
}