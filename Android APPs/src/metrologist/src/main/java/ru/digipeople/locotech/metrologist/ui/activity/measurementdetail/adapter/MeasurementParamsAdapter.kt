package ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Parameter
import ru.digipeople.locotech.metrologist.data.model.ParameterType
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import java.util.*
/**
 * Адаптер детальной информации замера
 */
class MeasurementParamsAdapter : BaseItemsRecyclerAdapter<Parameter<*>, MeasurementParamsAdapter.ViewHolder<*>>() {
    /**
     * Получение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return item.type.ordinal
    }
    /**
     * Создание холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
        val view = layoutInflater.inflate(R.layout.item_measurement_param, parent, false)
        return when (ParameterType.values()[viewType]) {
            ParameterType.BOOLEAN -> BooleanViewHolder(view)
            ParameterType.STRING -> TextViewHolder(view)
            ParameterType.NUMERIC -> NumericViewHolder(view)
            ParameterType.DATETIME -> DateTimeViewHolder(view)
        }
    }

    override fun onBindViewHolder(vh: ViewHolder<*>, position: Int) {
        val item = items[position]
        vh.bind(item)
    }
    /**
     * Холдер
     */
    abstract inner class ViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
        //region Views
        private val name: TextView = view.findViewById(R.id.name)
        private val value: TextView = view.findViewById(R.id.value)
        //endregion
        /**
         * Отображение значения
         */
        abstract fun toDisplayValue(value: T?): String?

        fun bind(parameter: Parameter<*>) {
            @Suppress("UNCHECKED_CAST")
            parameter as Parameter<T>

            value.text = toDisplayValue(parameter.value)
            name.text = parameter.name
        }
    }
    /**
     * Ходер булевого значения
     */
    inner class BooleanViewHolder(view: View) : ViewHolder<Boolean>(view) {
        override fun toDisplayValue(value: Boolean?): String? {
            return value?.toString() ?: ""
        }
    }
    /**
     * Холдер текстового значения
     */
    inner class TextViewHolder(view: View) : ViewHolder<String>(view) {
        override fun toDisplayValue(value: String?): String? {
            return value ?: ""
        }
    }
    /**
     * ХЗолдер числового значения
     */
    inner class NumericViewHolder(view: View) : ViewHolder<Float>(view) {
        override fun toDisplayValue(value: Float?): String? {
            return value?.toString() ?: ""
        }
    }
    /**
     * Холдер даты
     */
    inner class DateTimeViewHolder(view: View) : ViewHolder<Date>(view) {
        override fun toDisplayValue(value: Date?): String? {
            return value?.toString() ?: ""
        }
    }
}