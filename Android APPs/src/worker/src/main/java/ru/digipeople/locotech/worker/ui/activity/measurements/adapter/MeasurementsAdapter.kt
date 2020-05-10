package ru.digipeople.locotech.worker.ui.activity.measurements.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_measurement.*
import kotlinx.android.synthetic.main.item_measurement.view.*
import ru.digipeople.locotech.core.data.model.MeasureValueType
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.ui.valueeditor.BooleanValueEditor
import ru.digipeople.locotech.core.ui.valueeditor.TextValueEditor
import ru.digipeople.locotech.core.ui.valueeditor.ValueEditor
import ru.digipeople.locotech.worker.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Адаптер для замеров
 */
class MeasurementsAdapter : BaseItemsRecyclerAdapter<Measurement, MeasurementsAdapter.ViewHolder>() {
    val editableMeasurements: MutableMap<Int, EditableMeasurement> = mutableMapOf()
    var onCommentClickListener: ((measurement: Measurement) -> Unit)? = null

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    /**
     * Определени типа элемента
     */
    override fun getItemViewType(position: Int): Int = when (items[position].valueType) {
        MeasureValueType.STRING -> VIEW_TYPE_STRING_VALUE_MEASUREMENT
        MeasureValueType.BOOLEAN -> VIEW_TYPE_BOOLEAN_VALUE_MEASUREMENT
    }
    /**
     * Создание холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_measurement, parent, false)
        return when (viewType) {
            VIEW_TYPE_STRING_VALUE_MEASUREMENT -> StringValueMeasurementViewHolder(view)
            VIEW_TYPE_BOOLEAN_VALUE_MEASUREMENT -> BooleanValueMeasurementViewHolder(view)
            else -> throw IllegalStateException("Unsupported viewType=$viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        getItem(position)?.let { viewHolder.bind(it) }
    }
    /**
     * Холдер
     */
    abstract inner class ViewHolder(final override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        protected val value: EditText = containerView.measure_item_value_text
        protected val isEditable: Boolean
            get() = editableMeasurements.containsKey(adapterPosition)
        abstract val valueEditor: ValueEditor<*>

        //This method is used in order to setup value into editor
        abstract fun setValue(value: String)
        /**
         * Привязка данных к шаблону
         */
        fun bind(measurement: Measurement) {
            measure_item_title.text = context.getString(R.string.worker_title_text, measurement.measurementName, measurement.characteristicName)
            measure_item_measure_type_text.text = if (measurement.isHardware) {
                context.getString(R.string.measurement_hardware)
            } else context.getString(R.string.measurement_handmade)

            measure_item_value_text.isEnabled = !measurement.isHardware

            setValue(measurement.measurementValue)
            /**
             * Установка значения замера
             */
            valueEditor.onValueChangedListener = { value ->
                val editableMeasurement = editableMeasurements.getOrPut(adapterPosition) {
                    EditableMeasurement(measurement).apply { this.value = value }
                }

                editableMeasurement.value = value
            }

            resolveValueBackground(measurement)

            measure_item_norma_text.text = measurement.measurementNorm
            measure_item_worker_text.text = measurement.worker.name
            measure_item_date_text.text = measurement.measurementDate?.let {
                dateFormat.format(it)
            } ?: ""
            /**
             * Установка комментариев
             */
            if (measurement.measurementComment.text.isEmpty()) {
                comments_icon.visibility = View.GONE
                measure_item_comments.text = context.getString(R.string.measure_no_comments)
            } else {
                comments_icon.visibility = View.VISIBLE
                measure_item_comments.text = context.getString(R.string.measure_comments)
                measurement_comment_bg.setOnClickListener {
                    onCommentClickListener?.invoke(measurement)
                }
            }
        }
        /**
         * отрисовка цвета ячейки с замером в зависимости от типа и соответствия норме
         */
        private fun resolveValueBackground(measurement: Measurement) {
            val valueBackground = if (measurement.measurementValue.isNotEmpty()) {
                when {
                    measurement.isHardware -> context.getDrawable(R.color.appGray)
                    measurement.valueCompliance -> context.getDrawable(R.color.appGreen)
                    else -> context.getDrawable(R.color.appRed)
                }
            } else {
                context.getDrawable(R.color.appYellow)
            }

            measure_item_value_text.background = valueBackground
        }
    }
    /**
     * Холдер строкового значения замера
     */
    inner class StringValueMeasurementViewHolder(containerView: View) : ViewHolder(containerView) {
        override val valueEditor = TextValueEditor(value)

        override fun setValue(value: String) {
            valueEditor.value = if (isEditable) editableMeasurements[adapterPosition]?.value as? String else value
        }
    }
    /**
     * Холдер булевого значения замера
     */
    inner class BooleanValueMeasurementViewHolder(containerView: View) : ViewHolder(containerView) {
        override val valueEditor = BooleanValueEditor(value)

        override fun setValue(value: String) {
            valueEditor.value = if (isEditable) {
                editableMeasurements[adapterPosition]?.value as? Boolean
            } else {
                when (value) {
                    "true" -> true
                    "false" -> false
                    else -> null
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_STRING_VALUE_MEASUREMENT = 0;
        private const val VIEW_TYPE_BOOLEAN_VALUE_MEASUREMENT = 1;
    }
}