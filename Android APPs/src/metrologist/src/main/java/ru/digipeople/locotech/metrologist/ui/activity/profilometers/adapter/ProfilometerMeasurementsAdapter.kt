package ru.digipeople.locotech.metrologist.ui.activity.profilometers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.Profilometer
/**
 * Адаптер профилометров
 */
class ProfilometerMeasurementsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = AdapterData()
    var expandedProfilometers = mutableSetOf<String>()
    var onMeasureClickListener: ((profilerMeasurement: Measurement) -> Unit)? = null
    /**
     * Создане холдре ав зависимоси от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MEASUREMENT -> {
                val viewMeasurement = LayoutInflater.from(parent.context).inflate(R.layout.item_profilometer_measurement, parent, false)
                MeasurementViewHolder(viewMeasurement)
            }
            else -> {
                val viewProfilometer = LayoutInflater.from(parent.context).inflate(R.layout.item_profilometer, parent, false)
                ProfilometerViewHolder(viewProfilometer)
            }
        }
    }
    /**
     * получить число элементов в списке
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Определеить тип элемента
     */
    override fun getItemViewType(position: Int): Int {
        return if (items.isMeasurement(position)) {
            VIEW_TYPE_MEASUREMENT
        } else {
            VIEW_TYPE_PROFILER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items.isMeasurement(position)) {
            val vh = holder as MeasurementViewHolder
            vh.bind(items.getMeasurement(position))
        } else {
            val vh = holder as ProfilometerViewHolder
            vh.bind(items.getProfilometer(position))
        }
    }
    /**
     * Холдер замера
     */
    inner class MeasurementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.measurement_title)

        init {
            /**
             * Обработка нажатия на замер
             */
            itemView.setOnClickListener {
                onMeasureClickListener?.invoke(items.getMeasurement(adapterPosition))
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(profilerMeasurement: Measurement) {
            title.text = itemView.context.getString(R.string.profilometers_list_item, profilerMeasurement.number, profilerMeasurement.date)// Как должно формироваться название?
        }
    }
    /**
     * Холдер профилометра
     */
    inner class ProfilometerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.profilometer_title)

        init {
            /**
             * Обработка нажатия на профилометр
             */
            itemView.setOnClickListener {
                val profiler = items.getProfilometer(adapterPosition)
                toggleExpanded(profiler, adapterPosition)
            }
        }
        /**
         * разворачивание списка
         */
        private fun toggleExpanded(profiler: Profilometer, position: Int) {
            if (expandedProfilometers.remove(profiler.number)) {
                items.subList(position + 1, position + profiler.measurementList.size + 1).clear()
                notifyItemRangeRemoved(position + 1, profiler.measurementList.size)
            } else {
                expandedProfilometers.add(profiler.number)
                items.addAll(position + 1, profiler.measurementList)
                notifyItemRangeInserted(position + 1, profiler.measurementList.size)
            }
        }
        /**
         * Привязка данных
         */
        fun bind(profiler: Profilometer) {
            title.text = profiler.number
        }
    }

    companion object {
        private const val VIEW_TYPE_MEASUREMENT = 0
        private const val VIEW_TYPE_PROFILER = 1
    }
}
