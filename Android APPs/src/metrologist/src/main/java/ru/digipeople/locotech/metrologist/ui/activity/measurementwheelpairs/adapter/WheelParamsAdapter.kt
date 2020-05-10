package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.ParameterType
import ru.digipeople.locotech.metrologist.data.model.WheelParam
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.interactor.WheelParamValueValidator
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import java.util.*
import javax.inject.Inject

/**
 * Адаптер параметров колесных пар
 */
class WheelParamsAdapter @Inject constructor(private val wheelParamValueValidator: WheelParamValueValidator) : BaseItemsRecyclerAdapter<WheelParam<*>, WheelParamsAdapter.ViewHolder<*>>() {

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return item.type.ordinal
    }
    /**
     * Создание холдера в зависимости от типа
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
        val view = layoutInflater.inflate(R.layout.item_wheel_param, parent, false)
        return when (ParameterType.values()[viewType]) {
            ParameterType.BOOLEAN -> BooleanViewHolder(view)
            ParameterType.STRING -> TextViewHolder(view)
            ParameterType.NUMERIC -> NumericViewHolder(view)
            ParameterType.DATETIME -> DateTimeViewHolder(view)
        }
    }

    override fun onBindViewHolder(vh: ViewHolder<*>, position: Int) {
        vh.bind(items[position])
    }
    /**
     * Холдер
     */
    abstract inner class ViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
        private val paramName = view.findViewById<TextView>(R.id.param_name)
        private val leftValue = view.findViewById<TextView>(R.id.left_value)
        private val rightValue = view.findViewById<TextView>(R.id.right_value)
        private val normValue = view.findViewById<TextView>(R.id.norm_value)
        private val minValue = view.findViewById<TextView>(R.id.min_value)
        private val maxValue = view.findViewById<TextView>(R.id.max_value)
        /**
         * Инициализация
         */
        init {
            leftValue.isEnabled = false
            rightValue.isEnabled = false
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(wheelParam: WheelParam<*>) {
            @Suppress("UNCHECKED_CAST")
            wheelParam as WheelParam<T>

            paramName.text = wheelParam.name

            leftValue.text = toDisplayValue(wheelParam.leftValue)
            rightValue.text = toDisplayValue(wheelParam.rightValue)
            normValue.text = toDisplayValue(wheelParam.normValue)
            maxValue.text = toDisplayValue(wheelParam.maxValue)
            minValue.text = toDisplayValue(wheelParam.minValue)

            setColor(leftValue, wheelParam, true)
            setColor(rightValue, wheelParam, false)
        }
        /**
         * Установка цвета
         */
        private fun setColor(textView: TextView, wheelParam: WheelParam<T>, left: Boolean) {
            val result = if (left) {
                wheelParamValueValidator.validateLeft(wheelParam)
            } else {
                wheelParamValueValidator.validateRight(wheelParam)
            }
            val bgColor = when (result) {
                WheelParamValueValidator.Result.EMPTY -> Color.YELLOW
                WheelParamValueValidator.Result.OUT_OF_RANGE -> Color.RED
                WheelParamValueValidator.Result.VALID -> Color.GREEN
            }
            textView.setBackgroundColor(bgColor)
        }

        abstract fun toDisplayValue(value: T?): String?
    }
    /**
     * Холдер булевого значения
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
     * Холдер числового значения
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