package ru.digipeople.locotech.metrologist.data.api.entity

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.core.data.api.entity.PerformerEntity

/**
 * Модель замечания
 *
 * @author Michael Solenov
 */
class RemarkEntity {
    /**
     * идентификатор замечания из справочника
     */
    @SerializedName("remark_id")
    lateinit var id: String

    /**
     * название замечания
     */
    @SerializedName("remark_name")
    lateinit var name: String

    /**
     * дата и время создания, миллисекунды UTC
     */
    @SerializedName("remark_date")
    var date: Long = 0L

    /**
     * автор замечания
     */
    @SerializedName("remark_creator")
    lateinit var creator: PerformerEntity

    /**
     * полное название и номер секции и локомотива
     */
    @SerializedName("equipment_fullname")
    lateinit var equipmentFullName: String

    /**
     * идентификатор КП
     */
    @SerializedName("wheel_pair_id")
    lateinit var wheelPairId: String

    /**
     * номер бандажа
     */
    @SerializedName("flange_number")
    lateinit var flangeNumber: String

    /**
     * идентификатор источника замечаний
     */
    @SerializedName("source_id")
    lateinit var sourceId: String

    /**
     * наименование источника замечаний
     */
    @SerializedName("source_name")
    lateinit var sourceName: String
}