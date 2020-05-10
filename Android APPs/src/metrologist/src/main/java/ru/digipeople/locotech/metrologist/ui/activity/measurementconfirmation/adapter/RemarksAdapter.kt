package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Remark
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import java.text.SimpleDateFormat
import java.util.*
/**
 * адаптер замечаний
 */
class RemarksAdapter : BaseItemsRecyclerAdapter<Remark, RemarksAdapter.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd.mm.yyyy", Locale.getDefault())
    var expandedRemarks = mutableSetOf<String>()
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_remark, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val item = items[position]
        vh.bind(item)
    }
    /**
     * Холдер
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val linearAuthor = view.findViewById<LinearLayout>(R.id.linear_author)
        private val authorValue = view.findViewById<TextView>(R.id.author_value)
        private val linearSource = view.findViewById<LinearLayout>(R.id.linear_source)
        private val sourceValue = view.findViewById<TextView>(R.id.source_value)
        private val linearEquipment = view.findViewById<LinearLayout>(R.id.linear_equipment)
        private val equipmentValue = view.findViewById<TextView>(R.id.equipment_value)
        private val linearDate = view.findViewById<LinearLayout>(R.id.linear_date)
        private val dateValue = view.findViewById<TextView>(R.id.date_value)
        /**
         * Инициализация параметров
         */
        init {
            /**
             * Обработка нажатия на элемент списка
             */
            name.setOnClickListener {
                getItem(adapterPosition)?.let {
                    toggleExpanded(it)
                }
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(remark: Remark) {
            name.text = remark.name
            authorValue.text = remark.creator.name
            sourceValue.text = remark.sourceName
            equipmentValue.text = remark.equipmentFullName
            dateValue.text = dateFormat.format(remark.date)

            bindExpanded(remark)
        }
        /**
         * разворачмивание элемента
         */
        private fun bindExpanded(remark: Remark) {
            val expanded = expandedRemarks.contains(remark.id)
            val visibility = if (expanded) {
                View.VISIBLE
            } else {
                View.GONE
            }
            name.isSelected = expanded
            linearAuthor.visibility = visibility
            linearSource.visibility = visibility
            linearEquipment.visibility = visibility
            linearDate.visibility = visibility
        }

        private fun toggleExpanded(remark: Remark) {
            if (!expandedRemarks.remove(remark.id)) {
                expandedRemarks.add(remark.id)
            }
            bindExpanded(remark)
        }
    }
}