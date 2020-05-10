package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity
/**
 * Модель замера
 */
class MeasurementEntity {
    /**
     * Идентификатор замера
     */
    @SerializedName("measurement_id")
    lateinit var id: String
    /**
     * Номер замера
     */
    @SerializedName("measurement_number")
    var number = ""
    /**
     * Название секции
     */
    @SerializedName("section_name")
    lateinit var sectionName: String
    /**
     * номер секции
     */
    @SerializedName("section_number")
    var sectionNumber: String = ""
    /**
     *субномер секции
     */
    @SerializedName("section_subnumber")
    var sectionSubnumber: String = ""
    /**
     * Название оборудования
     */
    @SerializedName("equipment_name")
    lateinit var equipmentName: String
    /**
     * Тип замера
     */
    @SerializedName("measurement_type")
    lateinit var type: String
    /**
     * Вид замера
     */
    @SerializedName("measurement_category")
    lateinit var category: String
    /**
     * Дата и время замера
     */
    @SerializedName("measurement_date")
    var date: Long = 0
    /**
     * Признак ручного замера
     */
    @SerializedName("is_manual_measurement")
    var isManual: Boolean = false
    /**
     * Сотрудник
     */
    @SerializedName("measurement_creator")
    lateinit var creator: PerformerEntity
    /**
     * Номер профилометра
     */
    @SerializedName("profiler_number")
    lateinit var profilometerNumber: String
}
