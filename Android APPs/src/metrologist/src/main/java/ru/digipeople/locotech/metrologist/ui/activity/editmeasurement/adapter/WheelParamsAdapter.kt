package ru.digipeople.locotech.metrologist.ui.activity.editmeasurement.adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.core.ui.valueeditor.*
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.ParameterType
import ru.digipeople.locotech.metrologist.data.model.WheelParam
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.interactor.WheelParamValueValidator
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import java.util.*
import javax.inject.Inject

/**
 * адаптер редактирования данных по кп
 *
 * @author Michael Solenov
 */
class WheelParamsAdapter @Inject constructor(private val wheelParamValueValidator: WheelParamValueValidator) : BaseItemsRecyclerAdapter<WheelParam<*>, WheelParamsAdapter.ViewHolder<*>>() {

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return item.type.ordinal
    }
    /**
     * Создание холдера
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
     * Холдер замера
     */
    abstract inner class ViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
        //region Views
        val paramName = view.findViewById<TextView>(R.id.param_name)
        val leftValue = view.findViewById<TextView>(R.id.left_value)
        val rightValue = view.findViewById<TextView>(R.id.right_value)
        val normValue = view.findViewById<TextView>(R.id.norm_value)
        val minValue = view.findViewById<TextView>(R.id.min_value)
        val maxValue = view.findViewById<TextView>(R.id.max_value)
        //endregion
        private lateinit var wheelParam: WheelParam<T>
        @Suppress("LeakingThis")
        private val leftEditor = createEditor(leftValue)
        @Suppress("LeakingThis")
        private val rightEditor = createEditor(rightValue)
        /**
         * Инициализация
         */
        init {
            leftEditor.onValueChangedListener = {
                wheelParam.leftValue = it
                setColor(leftValue, wheelParam, true)
            }
            rightEditor.onValueChangedListener = {
                wheelParam.rightValue = it
                setColor(rightValue, wheelParam, false)
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(wheelParam: WheelParam<*>) {
            @Suppress("UNCHECKED_CAST")
            this.wheelParam = wheelParam as WheelParam<T>

            leftEditor.value = wheelParam.leftValue
            rightEditor.value = wheelParam.rightValue
            normValue.text = wheelParam.normValue?.toString() ?: ""
            minValue.text = wheelParam.minValue?.toString() ?: ""
            maxValue.text = wheelParam.maxValue?.toString() ?: ""
            paramName.text = wheelParam.name

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
                /**
                 * не заполнено
                 */
                WheelParamValueValidator.Result.EMPTY -> Color.YELLOW
                /**
                 * Вне нормы
                 */
                WheelParamValueValidator.Result.OUT_OF_RANGE -> Color.RED
                /**
                 * Валидное значение
                 */
                WheelParamValueValidator.Result.VALID -> Color.GREEN
            }
            textView.setBackgroundColor(bgColor)
        }

        abstract fun createEditor(textView: TextView): ValueEditor<T>
    }
    /**
     * Холдер булевного значения
     */
    inner class BooleanViewHolder(view: View) : ViewHolder<Boolean>(view) {
        override fun createEditor(textView: TextView) = BooleanValueEditor(textView)
                .apply {
                    positiveValue = context.getString(R.string.measurement_detail_wheel_param_yes)
                    negativeValue = context.getString(R.string.measurement_detail_wheel_param_no)
                }
    }
    /**
     * Холдер текстового значения
     */
    inner class TextViewHolder(view: View) : ViewHolder<String>(view) {
        override fun createEditor(textView: TextView) = TextValueEditor(textView)
    }
    /**
     * Холдер числового значения
     */
    inner class NumericViewHolder(view: View) : ViewHolder<Float>(view) {
        override fun createEditor(textView: TextView) = NumericValueEditor(textView)
    }
    /**
     * Холдер даты
     */
    inner class DateTimeViewHolder(view: View) : ViewHolder<Date>(view) {
        override fun createEditor(textView: TextView) = DateTimeValueEditor(textView)
    }
}