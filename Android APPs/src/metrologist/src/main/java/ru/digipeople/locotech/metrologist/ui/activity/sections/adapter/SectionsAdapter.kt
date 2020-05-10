package ru.digipeople.locotech.metrologist.ui.activity.sections.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Locomotive
import ru.digipeople.locotech.metrologist.data.model.Section
import ru.digipeople.locotech.metrologist.data.model.SectionState
import ru.digipeople.ui.adapter.BaseRecyclerAdapter

/**
 * Адаптер секций
 */
class SectionsAdapter : BaseRecyclerAdapter<RecyclerView.ViewHolder>() {
    var items = AdapterData(emptyList())
    var expandedLocomotives = mutableSetOf<String>()
    var onSectionClickListener: ((section: Section) -> Unit)? = null
    var currentSectionId: String? = null
    /**
     * Создание холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            /**
             * секция
             */
            VIEW_TYPE_SECTION -> {
                val viewSection = layoutInflater.inflate(R.layout.item_section, parent, false)
                SectionViewHolder(viewSection)
            }
            /**
             * локомотив
             */
            else -> {
                val viewLocomotive = layoutInflater.inflate(R.layout.item_locomotive, parent, false)
                LocomotiveViewHolder(viewLocomotive)
            }
        }
    }
    /**
     * Получение числа элементов
     */
    override fun getItemCount(): Int {
        return items.size
    }
    /**
     * Определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        return if (items.isLocomotive(position)) {
            VIEW_TYPE_LOCOMOTIVE
        } else {
            VIEW_TYPE_SECTION
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items.isLocomotive(position)) {
            val vh = holder as LocomotiveViewHolder
            vh.bind(items.getLocomotive(position))
        } else {
            val vh = holder as SectionViewHolder
            vh.bind(items.getSection(position))
        }
    }
    /**
     * разворачивание
     */
    fun toggleExpanded(locomotive: Locomotive, position: Int) {
        if (expandedLocomotives.remove(locomotive.id)) {
            notifyItemChanged(position, Unit)
            items.subList(position + 1, position + locomotive.sections.size + 1).clear()
            notifyItemRangeRemoved(position + 1, locomotive.sections.size)
        } else {
            notifyItemChanged(position, Unit)
            expandedLocomotives.add(locomotive.id)
            items.addAll(position + 1, locomotive.sections)
            notifyItemRangeInserted(position + 1, locomotive.sections.size)
        }
    }
    /**
     * Холдер локомотива
     */
    inner class LocomotiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val locoTitle: TextView = view.findViewById(R.id.locomotive_title)
        private val expandableIcon: ImageView = view.findViewById(R.id.expandableIcon)

        init {
            /**
             * обработка нажатия на локомотив
             */
            itemView.setOnClickListener {
                val locomotive = items.getLocomotive(adapterPosition)
                toggleExpanded(locomotive, adapterPosition)
            }
        }
        /**
         * Привязк аданных
         */
        fun bind(locomotive: Locomotive) {
            locoTitle.text = if (locomotive.name.isNotBlank()) {
                itemView.context.getString(R.string.sections_locomotive_title, locomotive.name, locomotive.number)
            } else {
                itemView.context.getString(R.string.sections_locomotive_title_without_name, locomotive.number)
            }
            expandableIcon.isSelected = expandedLocomotives.contains(locomotive.id)
        }
    }
    /**
     * Холдер секции
     */
    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sectionDot: View = view.findViewById(R.id.section_view_dot)
        private val sectionTitle: TextView = view.findViewById(R.id.measurement_title)
        private val sectionNumber: TextView = view.findViewById(R.id.section_number)
        private val sectionRepairTypes: TextView = view.findViewById(R.id.section_repair_type)

        init {
            /**
             * Обработка нажатия на секцию
             */
            itemView.setOnClickListener {
                onSectionClickListener?.invoke(items.getSection(adapterPosition))
            }
        }

        /**
         * Привязка данных к шаблону
         */
        fun bind(section: Section) {
            sectionTitle.text = itemView.context.getString(R.string.sections_section_title, section.number)
            sectionNumber.text = itemView.context.getString(R.string.section_section_number, section.subNumber)
            sectionNumber.visibility = if (section.subNumber.isNotEmpty()) View.VISIBLE else View.GONE
            sectionRepairTypes.text = context.getString(R.string.section_repair_type, section.repairType?.name)
            sectionDot.background = when (section.state) {
                SectionState.UNDEFINED -> context.getDrawable(R.drawable.red_dot)
                SectionState.WAITING -> context.getDrawable(R.drawable.yellow_dot)
                SectionState.IN_SERVICE -> context.getDrawable(R.drawable.green_dot)
            }

            itemView.isSelected = section.id == currentSectionId
        }
    }

    companion object {
        private const val VIEW_TYPE_LOCOMOTIVE = 0
        private const val VIEW_TYPE_SECTION = 1
    }
}