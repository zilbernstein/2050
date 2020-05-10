package ru.digipeople.locotech.master.ui.activity.measurement.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_measure_works.*
import kotlinx.android.synthetic.main.item_measure_works_title.*
import ru.digipeople.locotech.core.data.model.MeasureValueType
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.master.R
import ru.digipeople.ui.adapter.BaseRecyclerAdapter
import ru.digipeople.utils.adapter.BaseDiffUtilCallback
import java.text.SimpleDateFormat
import java.util.*

/**
 * Адаптер измерений
 *
 * @author Sostavkin Grisha
 */
class MeasurementAdapter : BaseRecyclerAdapter<RecyclerView.ViewHolder>() {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    //region Other
    private var adapterData = AdapterData()
    var itemClickListener: ((measurement: Measurement) -> Unit)? = null
    var commentClickListener: ((measurement: Measurement) -> Unit)? = null
    //endregion
    /**
     * действия при создании холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        /**
         * выбор холдера по типу элемента
         */
        return when (viewType) {
            /**
             * заголовок
             */
            VIEW_TYPE_MEASUREMENT_TITLE -> TitleViewHolder(layoutInflater.inflate(R.layout.item_measure_works_title, parent, false))
            /**
             * замер
             */
            VIEW_TYPE_MEASUREMENT -> MeasurementViewHolder(layoutInflater.inflate(R.layout.item_measure_works, parent, false))
            /**
             * разделитель
             */
            VIEW_TYPE_DIVIDER -> DividerViewHolder(layoutInflater.inflate(R.layout.item_divider, parent, false))
            else -> throw IllegalArgumentException("Unsupported viewType $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is TitleViewHolder -> viewHolder.bind(adapterData.getMeasurementTitleView(position))
            is MeasurementViewHolder -> viewHolder.bind(adapterData.getMeasurementView(position))
        }
    }
    /**
     * Определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            adapterData.isMeasurementTitleView(position) -> VIEW_TYPE_MEASUREMENT_TITLE
            adapterData.isMeasurementView(position) -> VIEW_TYPE_MEASUREMENT
            adapterData.isDividerView(position) -> VIEW_TYPE_DIVIDER
            else -> throw IllegalArgumentException("Unsupported position = $position")
        }
    }
    /**
     * получение числа элементов в списке
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * установка данных
     */
    fun setData(newData: AdapterData) {
        val diffUtilResult = DiffUtil.calculateDiff(DiffUtilCallback(adapterData, newData))
        this.adapterData = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    //region ViewHolders
    /**
     * холдер заголовка
     */
    inner class TitleViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: MeasurementTitleModel) {
            measure_list_item_title.text = item.title
        }
    }
    /**
     * холдер замера
     */
    inner class MeasurementViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: MeasurementModel) {
            /**
             * привязка данных к шаблону
             */
            if (!item.measurement.isHardware) {
                measure_list_item_value.setOnClickListener { itemClickListener?.invoke(item.measurement) }
            }

            if (item.measurement.characteristicName.isNotEmpty() && item.measurement.measurementName.isNotEmpty()) {
                measure_list_item_name.text = context.getString(R.string.measurement_name_value, item.measurement.measurementName, item.measurement.characteristicName)
            } else measure_list_item_name.text = context.getString(R.string.measurement_name_value_empty, item.measurement.measurementName)

            measure_list_item_normal.text = if (item.measurement.measurementNorm.isNotEmpty()) {
                item.measurement.measurementNorm
            } else NO_DATA_PLACEHOLDER
            measure_list_item_worker.text = if (item.measurement.worker.name.isNotEmpty()) {
                item.measurement.worker.name
            } else NO_DATA_PLACEHOLDER
            measure_list_item_date.text = item.measurement.measurementDate?.let { dateFormat.format(it) }
                    ?: NO_DATA_PLACEHOLDER
            /**
             * управление видимостью комментариев
             */
            if (item.measurement.measurementComment.text.isEmpty()) {
                measure_list_item_no_comment.visibility = View.VISIBLE
                measure_list_item_view_comment.visibility = View.INVISIBLE
            } else {
                measure_list_item_no_comment.visibility = View.INVISIBLE
                measure_list_item_view_comment.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { commentClickListener?.invoke(item.measurement) }
                }
            }
            /**
             * отображение типа замера
             */
            measure_list_item_type.text = if (item.measurement.isHardware) {
                context.getString(R.string.measurement_hardware)
            } else {
                context.getString(R.string.measurement_handmade)
            }
            /**
             * отображение значения, если зтип замера булевый
             */
            val measurementValue = item.measurement.measurementValue
            measure_list_item_value.text = when (item.measurement.valueType) {
                MeasureValueType.BOOLEAN -> when (measurementValue) {
                    "true" -> context.getString(R.string.measurement_value_true)
                    "false" -> context.getString(R.string.measurement_value_false)
                    else -> NO_DATA_PLACEHOLDER
                }
                MeasureValueType.STRING -> if (measurementValue.isNotEmpty()) measurementValue else NO_DATA_PLACEHOLDER
                else -> NO_DATA_PLACEHOLDER
            }

            resolveValueBackground(item.measurement)
        }
        /**
         * раскраска поля значения замер ав зависимости от  соответствия значения норме
         */
        private fun resolveValueBackground(measurement: Measurement) {
            val valueBackground = if (measurement.measurementValue.isNotEmpty()) {
                when {
                    /**
                     * аппаратный
                     */
                    measurement.isHardware -> context.getDrawable(R.color.appGray)
                    /**
                     * соответствует
                     */
                    measurement.valueCompliance -> context.getDrawable(R.color.appGreen)
                    /**
                     * не соответствует
                     */
                    else -> context.getDrawable(R.color.appRed)
                }
            } else {
                /**
                 * не задан замер
                 */
                context.getDrawable(R.color.appYellow)
            }

            measure_list_item_value.background = valueBackground
        }
    }

    inner class DividerViewHolder(view: View) : RecyclerView.ViewHolder(view)
    //endregion

    //Large checks here cause of large measurement class
    /**
     * обработка отображающихся элементов при перерисовке
     */
    class DiffUtilCallback(oldItems: List<Any>, newItems: List<Any>) : BaseDiffUtilCallback<Any>(oldItems, newItems) {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
            (oldItem is MeasurementModel && newItem is MeasurementModel) -> {
                oldItem.measurement.measurementId == newItem.measurement.measurementId &&
                        oldItem.measurement.measurementValue == newItem.measurement.measurementValue &&
                        oldItem.measurement.worker.id == newItem.measurement.worker.id
            }
            else -> oldItem == newItem
        }
        /**
         * сравнение двух элементов
         */
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = false
    }

    companion object {
        const val VIEW_TYPE_MEASUREMENT_TITLE = 0
        const val VIEW_TYPE_MEASUREMENT = 1
        const val VIEW_TYPE_DIVIDER = 2

        private const val NO_DATA_PLACEHOLDER = "-"
    }
}