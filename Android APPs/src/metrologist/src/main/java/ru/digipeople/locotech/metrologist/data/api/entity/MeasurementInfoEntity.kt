package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
/**
 * Модель информации о замере
 */
class MeasurementInfoEntity {
    /**
     * Замер
     */
    @SerializedName("measurement")
    lateinit var measurement: MeasurementEntity
    /**
     * Основные показатели по замеру
     */
    @SerializedName("measurement_parameters")
    lateinit var parameters: List<ParameterEntity>
}