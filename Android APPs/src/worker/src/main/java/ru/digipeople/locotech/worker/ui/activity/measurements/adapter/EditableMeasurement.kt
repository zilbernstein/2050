package ru.digipeople.locotech.worker.ui.activity.measurements.adapter

import ru.digipeople.locotech.core.data.model.Measurement

/**
 * Wrapper which enables to store edited props of measurement
 */
class EditableMeasurement(val measurement: Measurement) {
    /**
     * Значение
     */
    var value: Any? = null
    /**
     * Комментарий
     */
    var comment: String? = null
}