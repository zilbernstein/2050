package ru.digipeople.locotech.metrologist.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Measurement
import java.text.SimpleDateFormat
import java.util.*
/**
 * view элемента редактирования данных по кп
 *
 * @author Michael Solenov
 */
class MeasurementInfoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    //region Views
    private val sectionValue: TextView
    private val typeValue: TextView
    private val categoryValue: TextView
    private val measurementValue: TextView
    private val sourceValue: TextView
    private val workerValue: TextView
    private val dateTimeValue: TextView
    //endregion
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy / HH:mm", Locale.getDefault())
    /**
     * Инициализация
     */
    init {
        View.inflate(getContext(), R.layout.view_measurement_info, this)
        sectionValue = findViewById(R.id.section_value)
        typeValue = findViewById(R.id.type_value)
        categoryValue = findViewById(R.id.kind_value)
        measurementValue = findViewById(R.id.measurement_value)
        sourceValue = findViewById(R.id.source_value)
        workerValue = findViewById(R.id.worker_value)
        dateTimeValue = findViewById(R.id.date_time_value)
        clipToPadding = false
    }
    /**
     * Установка замера
     */
    fun setMeasurement(measurement: Measurement) {
        sectionValue.text = measurement.sectionName
        typeValue.text = measurement.type
        categoryValue.text = measurement.category
        if (measurement.isManual) {
            measurementValue.text = context.getString(R.string.measurement_confirmation_manual_measurement)
            sourceValue.text = context.getString(R.string.measurement_confirmation_manual_source)
        } else {
            measurementValue.text = context.getString(R.string.measurement_confirmation_not_manual_measurement)
            sourceValue.text = context.getString(R.string.measurement_confirmation_not_manual_source)
        }
        workerValue.text = measurement.creator.name
        dateTimeValue.text = dateFormat.format(measurement.date)
    }
}