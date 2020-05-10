package ru.digipeople.locotech.metrologist.data.api.response

import com.google.gson.annotations.SerializedName
import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementEntity
import ru.digipeople.locotech.metrologist.data.api.entity.RemarkEntity
import ru.digipeople.locotech.metrologist.data.api.entity.WheelPairShortEntity

/**
 * Модель ответа метода measurement_info_before_complete МП Мониторинг КП
 *
 * @author Michael Solenov
 */
class MeasurementInfoBeforeCompleteResponse {
    /**
     * Замер
     */
    @SerializedName("measurement")
    lateinit var measurement: MeasurementEntity
    /**
     * Колесные пары
     */
    @SerializedName("wheel_pairs")
    lateinit var wheelPairs: List<WheelPairShortEntity>
    /**
     * Потенциальные замечания
     */
    @SerializedName("remarks")
    lateinit var remarks: List<RemarkEntity>
}