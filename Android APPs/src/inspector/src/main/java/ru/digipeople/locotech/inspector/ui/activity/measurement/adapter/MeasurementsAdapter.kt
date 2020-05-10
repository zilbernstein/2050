package ru.digipeople.locotech.inspector.ui.activity.measurement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.inspector.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Адаптер замеров
 * @author Sostavkin Grisha
 */
class MeasurementsAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    //region Other
    private var adapterData = AdapterData()
    var itemClickListener: ((measurement: Measurement) -> Unit)? = null
    var commentClickListener: ((measurement: Measurement) -> Unit)? = null
    //endregion
    /**
     * создание холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            /**
             * заголовок
             */
            VIEW_TYPE_TITLE -> {
                val titleView = LayoutInflater.from(parent.context).inflate(R.layout.item_measure_works_title, parent, false)
                TitleViewHolder(titleView)
            }
            /**
             * замер
             */
            VIEW_TYPE_DATA -> {
                val measurementView = LayoutInflater.from(parent.context).inflate(R.layout.item_measure_works, parent, false)
                MeasurementViewHolder(measurementView)
            }
            /**
             * разделитель
             */
            VIEW_TYPE_DIVIDER -> {
                val viewDivider = LayoutInflater.from(parent.context).inflate(R.layout.item_divider, parent, false)
                DividerViewHolder(viewDivider)
            }
            else -> {
                val viewDivider = LayoutInflater.from(parent.context).inflate(R.layout.item_divider, parent, false)
                DividerViewHolder(viewDivider)
            }
        }
    }

    override fun onBindViewHolder(vh: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when {
            adapterData.isTitleView(position) -> {
                val holder = vh as TitleViewHolder
                holder.bind(adapterData.getTitleView(position))
            }
            adapterData.isMeasurementView(position) -> {
                val holder = vh as MeasurementViewHolder
                holder.bind(adapterData.getMeasurementView(position))
            }
            adapterData.isDividerView(position) -> {
                vh as DividerViewHolder
            }
        }
    }
    /**
     * определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            adapterData.isTitleView(position) -> VIEW_TYPE_TITLE
            adapterData.isMeasurementView(position) -> VIEW_TYPE_DATA
            adapterData.isDividerView(position) -> VIEW_TYPE_DIVIDER
            else -> throw IllegalArgumentException("Unsupported position = $position")
        }
    }
    /**
     * получение кол-ва элементов в списке
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * установка данных
     */
    fun setData(data: AdapterData) {
        val diffUtilCallback = DiffUtilCallback(adapterData, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.adapterData = data
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * Холдер заголовка
     */
    inner class TitleViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val titleTextView = view.findViewById<TextView>(R.id.measure_list_item_title)

        fun bind(title: TitleModel) {
            titleTextView.text = title.title
        }
    }
    /**
     * холдер замера
     */
    inner class MeasurementViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.measure_list_item_indicator)
        private val value = view.findViewById<TextView>(R.id.measure_list_item_value)
        private val norm = view.findViewById<TextView>(R.id.measure_list_item_normal)
        private val worker = view.findViewById<TextView>(R.id.measure_list_item_worker)
        private val date = view.findViewById<TextView>(R.id.measure_list_item_date)
        private val comment = view.findViewById<TextView>(R.id.measure_list_item_comment)
        private val eyeImage = view.findViewById<ImageView>(R.id.imageView)
        private val type = view.findViewById<TextView>(R.id.measure_list_item_type)
        /**
         * Привязка данных к шаблону
         */
        fun bind(measurementModel: MeasurementModel) {
            if (measurementModel.measurement.characteristicName.isNotEmpty() && measurementModel.measurement.measurementName.isNotEmpty()) {
                name.text = itemView.context.getString(R.string.measurement_name_value, measurementModel.measurement.measurementName, measurementModel.measurement.characteristicName)
            } else {
                name.text = itemView.context.getString(R.string.measurement_name_value_empty, measurementModel.measurement.measurementName)
            }
            norm.text = measurementModel.measurement.measurementNorm
            worker.text = measurementModel.measurement.worker.name
            date.text = dateToString(measurementModel.measurement.measurementDate)

            if (measurementModel.measurement.measurementComment.text.isEmpty()) {
                comment.visibility = View.VISIBLE
                eyeImage.visibility = View.INVISIBLE
            } else {
                comment.visibility = View.INVISIBLE
                eyeImage.visibility = View.VISIBLE
                eyeImage.setOnClickListener {
                    commentClickListener?.invoke(measurementModel.measurement)
                }
            }
            /**
             * тип замера
             */
            type.text = if (measurementModel.measurement.isHardware) {
                itemView.context.getText(R.string.measurement_type_true)
            } else {
                itemView.context.getText(R.string.measurement_type_false)
            }

            when (measurementModel.measurement.measurementValue) {
                "true" -> value.text = itemView.context.getString(R.string.measurement_value_true)
                "false" -> value.text = itemView.context.getString(R.string.measurement_value_false)
                else -> value.text = measurementModel.measurement.measurementValue
            }
            /**
             * раскраска фона ячейки
             */
            when {
                measurementModel.measurement.valueCompliance.toString().isEmpty() -> value.background = itemView.context.getDrawable(R.color.appYellow)
                measurementModel.measurement.valueCompliance -> value.background = itemView.context.getDrawable(R.color.appGreen)
                else -> value.background = itemView.context.getDrawable(R.color.appRed)
            }



            itemClickListener?.let {
                value.setOnClickListener {
                    it(measurementModel.measurement)
                }
            }
        }

    }
    /**
     * преобразование даты в строку
     */
    fun dateToString(date: Date?): String {
        return if (date == null) "" else dateFormat.format(date)
    }

    fun setItemAction(action: (Measurement) -> Unit) {
        this.itemClickListener = action
    }
    /**
     * Холдер разделителя
     */
    inner class DividerViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_TITLE = 0
        private const val VIEW_TYPE_DATA = 1
        private const val VIEW_TYPE_DIVIDER = 2
    }
}
