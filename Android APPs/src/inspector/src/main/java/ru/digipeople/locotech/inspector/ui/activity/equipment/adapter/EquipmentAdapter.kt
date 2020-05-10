package ru.digipeople.locotech.inspector.ui.activity.equipment.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_section.*
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.*
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.EquipmentItem
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.SectionItem
import ru.digipeople.ui.adapter.BaseRecyclerAdapter
import ru.digipeople.utils.DateUtils
import java.util.*

/**
 * Адаптер локомотивов
 */
class EquipmentAdapter : BaseRecyclerAdapter<RecyclerView.ViewHolder>() {

    var items = AdapterData()
        set(value) {
            field = value
            notifyAdapterDataSetChanged()
        }
    var onSectionClickListener: ((equipment: Equipment, section: Section) -> Unit)? = null
    var onLineEquipmentClickListener: ((equipment: Equipment) -> Unit)? = null

    // Время последнего обновления данных адаптера.
    // Вместе с AutoRefreshDelegate служит для отсчета таймеров работ
    private var lastTimeUpdate = Date()
    private var timeDelta = 0L
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            /**
             * секция
             */
            VIEW_TYPE_SECTION -> {
                val viewSection = layoutInflater.inflate(R.layout.item_section, parent, false)
                return SectionViewHolder(viewSection)
            }
            /**
             * разделитель
             */
            VIEW_TYPE_DIVIDER -> {
                val viewDivider = layoutInflater.inflate(R.layout.recycler_divider, parent, false)
                return DividerViewHolder(viewDivider)
            }
            /**
             * Оборудование
             */
            VIEW_TYPE_LINE_EQUIPMENT -> {
                val viewLineEquipment = layoutInflater.inflate(R.layout.item_line_equipment, parent, false)
                return LineEquipmentViewHolder(viewLineEquipment)
            }
            else -> {
                val viewEquipment = layoutInflater.inflate(R.layout.item_locomotive, parent, false)
                return EquipmentViewHolder(viewEquipment)
            }
        }
    }
    /**
     * Подсчет числа элементов
     */
    override fun getItemCount(): Int {
        return items.size
    }
    /**
     * определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        if (items.isEquipmentData(position)) {
            return VIEW_TYPE_EQUIPMENT
        } else if (items.isLineEquipmentData(position)) {
            return VIEW_TYPE_LINE_EQUIPMENT
        } else if (items.isSection(position)) {
            return VIEW_TYPE_SECTION
        } else if (items.isDividerView(position)) {
            return VIEW_TYPE_DIVIDER
        } else {
            throw IllegalArgumentException("Unsupported item type at position $position")
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        if (items.isEquipmentData(position)) {
            val holder = vh as EquipmentViewHolder
            holder.bind(items.getEquipment(position))
        } else if (items.isSection(position)) {
            val holder = vh as SectionViewHolder
            holder.bind(items.getSection(position))
        } else if (items.isLineEquipmentData(position)) {
            val holder = vh as LineEquipmentViewHolder
            holder.bind(items.getLineEquipment(position))
        }
    }
    /**
     * Установка прогресса и раскраска
     */
    private fun setUpProgressBar(progress: Int, progressBar: ProgressBar) {
        when (progress) {
            /**
             * зеленый
             */
            in 0..90 -> {
                progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.green_progress_bar_drawable)
            }
            /**
             * желтый
             */
            in 90..94 -> {
                progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.yellow_progress_bar_drawable)
            }
            /**
             * красный
             */
            else -> {
                progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.red_progress_bar_drawable)
            }
        }
        progressBar.setProgress(progress)
    }

    /**
     * разворачивание оборудования
     */
    private fun expandEquipment(equipmentView: EquipmentItem, position: Int) {
        if (equipmentView.isExpanded) {
            items.removeAll(equipmentView.sections)
        } else {
            items.addAll(position + 1, equipmentView.sections)
        }
        notifyDataSetChanged()
        equipmentView.isExpanded = !equipmentView.isExpanded
    }

    fun notifyAdapterDataSetChanged() {
        val newDate = Date()
        timeDelta = newDate.time - lastTimeUpdate.time
        lastTimeUpdate = newDate

        notifyDataSetChanged()
    }
    /**
     * Холдер оборудования
     */
    inner class EquipmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val equipmentTitle: TextView
        private val equipmentTime: TextView
        private val equimentPrgressBar: ProgressBar
        private val equipmentPercent: TextView
        private val eqipmentArrow: ImageView
        private val locomotiveMainInfo: ConstraintLayout
        private val worksCount: TextView

        init {
            equipmentTitle = view.findViewById(R.id.locomotive_title)
            equipmentTime = view.findViewById(R.id.locomotive_time)
            equimentPrgressBar = view.findViewById(R.id.locomotive_detail_progress_bar)
            equipmentPercent = view.findViewById(R.id.locomotive_percent)
            eqipmentArrow = view.findViewById(R.id.locomotive_arrow)
            locomotiveMainInfo = view.findViewById(R.id.item_locomotive_main_info)
            worksCount = view.findViewById(R.id.equipment_works_counts)

            locomotiveMainInfo.setOnClickListener { expandEquipment(items.getEquipment(adapterPosition), adapterPosition) }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(equipmentView: EquipmentItem) {
            val equipment = equipmentView.equipment
            /**
             * изменение стрелки
             */
            if (equipmentView.isExpanded) {
                eqipmentArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_drop_up))
            } else {
                eqipmentArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_drop_down))
            }

            worksCount.text = WorksCountStringBuilder.build(equipment.worksCount, context)
            equipment.timeLeft = Date(equipment.timeLeft.time - timeDelta)
            val rawRemainingTime = equipment.timeLeft.time
            equipmentTime.text = DateUtils.convertToString(rawRemainingTime)

            if (rawRemainingTime >= 0L) {
                equipmentTime.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
            } else {
                equipmentTime.setTextColor(ContextCompat.getColor(context, R.color.appRed))
            }

            equipmentTitle.text = context.getString(R.string.equipment_activity_locomotive_title, equipment.number, equipment.subNumber)
            setUpProgressBar(equipment.progress, equimentPrgressBar)
            equipmentPercent.setText(context.getResources().getString(R.string.equipment_activity_percent, equipment.progress))
        }
    }
    /**
     * Холдер линейного оборудования
     */
    inner class LineEquipmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val equipmentTitle: TextView
        private val equipmentTime: TextView
        private val equimentPrgressBar: ProgressBar
        private val equipmentPercent: TextView
        private val lineEquipmentInfo: ConstraintLayout

        init {
            equipmentTitle = view.findViewById(R.id.line_equipment_title)
            equipmentTime = view.findViewById(R.id.line_equipment_time)
            equimentPrgressBar = view.findViewById(R.id.line_equipment_progress_bar)
            equipmentPercent = view.findViewById(R.id.line_equipment_percent)
            lineEquipmentInfo = view.findViewById(R.id.line_equipment_main_info)

            lineEquipmentInfo.setOnClickListener { onLineEquipmentClickListener?.invoke((items.getLineEquipment(adapterPosition))) }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(equipment: Equipment) {
            if (equipment.isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedEquipment))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.appWhite))
            }

            equipment.timeLeft = Date(equipment.timeLeft.time - timeDelta)
            val rawRemainingTime = equipment.timeLeft.time
            equipmentTime.text = DateUtils.convertToString(rawRemainingTime)

            if (rawRemainingTime >= 0L) {
                equipmentTime.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
            } else {
                equipmentTime.setTextColor(ContextCompat.getColor(context, R.color.appRed))
            }

            if (equipment.timeRequired.time == 0L) {
                equipmentPercent.text = ""
            } else {
                /**
                 * текущий прогресс
                 */
                val currentProgress = (equipment.timeLeft.time * 100) / equipment.timeRequired.time
                setUpProgressBar(equipment.progress, equimentPrgressBar)
                equipmentPercent.text = context.getResources().getString(R.string.equipment_activity_percent, currentProgress)
            }
            equipmentTitle.text = context.getString(R.string.equipment_activity_locomotive_title, equipment.name, equipment.number)
        }
    }
    /**
     * Холдер секции
     */
    inner class SectionViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            /**
             * Обработка клика на секцию
             */
            item_section.setOnClickListener {
                val sectionModel = items.getSection(adapterPosition)
                onSectionClickListener?.invoke(sectionModel.equipment, sectionModel.section)
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(sectionItem: SectionItem) {
            val section = sectionItem.section

            if (section.isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedEquipment))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.appWhite))
            }

            section_view_dot.background = when (section.state) {
                SectionState.WAITING -> context.getDrawable(R.drawable.yellow_dot)
                SectionState.IN_SERVICE -> context.getDrawable(R.drawable.green_dot)
                SectionState.UNDEFINED -> context.getDrawable(R.drawable.red_dot)
            }

            section_works_count.text = WorksCountStringBuilder.build(section.worksCount, context)
            section_repair_type.text = context.getString(R.string.section_repair_type, section.repairType?.name)
            section_title.text = context.getString(R.string.equipment_activity_section_title, section.name, section.number, section.subNumber)
            section_type.text = section.type
            /**
             * отрисовка иконок
             */
            section.equipmentHealth.forEach { equipmentHealth ->
                val equipmentHealthView = when (equipmentHealth.position) {
                    1 -> first_health_icon
                    2 -> second_health_icon
                    3 -> third_health_icon
                    else -> fourth_health_icon
                }
                /**
                 * установка фона иконок
                 */
                equipmentHealthView.setBackgroundColor(when (equipmentHealth.status) {
                    EquipmentHealthStatus.GREEN -> ContextCompat.getColor(context, R.color.appGreen)
                    EquipmentHealthStatus.YELLOW -> ContextCompat.getColor(context, R.color.appYellow)
                    else -> ContextCompat.getColor(context, R.color.appRed)
                })

                equipmentHealthView.setImageDrawable(when (equipmentHealth.type) {
                    EquipmentHealthType.TIMELIMITS -> ContextCompat.getDrawable(context, R.drawable.time)
                    EquipmentHealthType.STAFF -> ContextCompat.getDrawable(context, R.drawable.pers)
                    EquipmentHealthType.EQUIPMENTS -> ContextCompat.getDrawable(context, R.drawable.ob)
                    EquipmentHealthType.GOODS -> ContextCompat.getDrawable(context, R.drawable.ob)
                    else -> ContextCompat.getDrawable(context, R.drawable.others)
                })
            }
        }
    }
    /**
     * Холдер разделителя
     */
    inner class DividerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_EQUIPMENT = 0
        private const val VIEW_TYPE_LINE_EQUIPMENT = 1
        private const val VIEW_TYPE_SECTION = 2
        private const val VIEW_TYPE_DIVIDER = 3
    }
}