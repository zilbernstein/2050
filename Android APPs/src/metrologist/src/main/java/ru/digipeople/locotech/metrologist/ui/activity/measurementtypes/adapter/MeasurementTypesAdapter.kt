package ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.MeasurementCategory
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject
/**
 * Адаптер типов замеров
 */
class MeasurementTypesAdapter @Inject constructor() : BaseItemsRecyclerAdapter<MeasurementCategory, MeasurementTypesAdapter.ViewHolder>() {
    var onItemClickListener: ((measurementType: MeasurementCategory) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_measurement_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val measurementType = items[position]
        vh.bind(measurementType)
    }
    /**
     * Холдер типа замера
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val checkBox: CheckBox = view.findViewById(R.id.checkbox)

        init {
            /**
             * Обработка нажатия на элемент списка
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener?.invoke(it)
                }
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(measurementType: MeasurementCategory) {
            title.text = measurementType.name
            checkBox.isChecked = measurementType.hasMeasurement
        }
    }
}
