package ru.digipeople.locotech.master.ui.activity.equipment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_line_equipment.*
import kotlinx.android.synthetic.main.item_locomotive.*
import kotlinx.android.synthetic.main.item_section.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.*
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.DividerItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.EquipmentItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.LineEquipmentItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.SectionItem
import ru.digipeople.utils.DateUtils
import java.util.*
import javax.inject.Inject

/**
 * Адаптер локомотивов на учатске
 *
 * @author Kashonkov Nikita
 */
class EquipmentAdapter @Inject constructor(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //region Other
    private var adapterData = AdapterData()
    var sectionClickListner: ((section: Section) -> Unit)? = null
    var lineEquipmentClickListener: ((equipment: Equipment) -> Unit)? = null

    // Время последнего обновления данных адаптера. Вместе с AutoredreshDelegate служит для отсчета таймеров работ
    private var lastTimeUpdate = Date()
    private var timeDelta = 0L
    //end Region
    /**
     * операции при создании холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when (viewType) {
            /**
             * определение типа
             */
            VIEW_TYPE_EQUIPMENT -> EquipmentViewHolder(inflater.inflate(R.layout.item_locomotive, parent, false))
            VIEW_TYPE_SECTION -> SectionViewHolder(inflater.inflate(R.layout.item_section, parent, false))
            VIEW_TYPE_LINE_EQUIPMENT -> LineEquipmentViewHolder(inflater.inflate(R.layout.item_line_equipment, parent, false))
            VIEW_TYPE_DIVIDER -> DividerViewHolder(inflater.inflate(R.layout.recycler_divider, parent, false))
            else -> throw IllegalStateException("Unable to create view holder by view type = $viewType")
        }
    }
    /**
     * определение количества элементов
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * получение типа в заданной позиции
     */
    override fun getItemViewType(position: Int): Int {
        return when (adapterData[position]) {
            is EquipmentItem -> VIEW_TYPE_EQUIPMENT
            is SectionItem -> VIEW_TYPE_SECTION
            is LineEquipmentItem -> VIEW_TYPE_LINE_EQUIPMENT
            is DividerItem -> VIEW_TYPE_DIVIDER
            else -> throw IllegalArgumentException("Unable to get view type by position = $position")
        }
    }
    /**
     * оопределение холдера в зависимости от типа элемента
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is EquipmentViewHolder -> viewHolder.bind(adapterData.getEquipment(position))
            is SectionViewHolder -> viewHolder.bind(adapterData.getSectionData(position))
            is LineEquipmentViewHolder -> viewHolder.bind(adapterData.getLineEquipment(position))
        }
    }
    /**
     * установка данных
     */
    fun setData(list: AdapterData) {
        val diffUtilCallback = DiffUtilCallback(adapterData, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.adapterData = list
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * разворачивание локомотива при нажатии
     */
    private fun expandEquipment(equipmentView: EquipmentItem, position: Int) {
        if (equipmentView.isExpanded) {
            adapterData.removeAll(equipmentView.sectionData)
        } else {
            adapterData.addAll(position + 1, equipmentView.sectionData)
        }
        notifyDataSetChanged()
        equipmentView.isExpanded = !equipmentView.isExpanded
    }
    /**
     * обновление данных в адаптере
     */
    fun notifyAdapterDataSetChanged() {
        val newDate = Date()
        timeDelta = newDate.time - lastTimeUpdate.time
        lastTimeUpdate = newDate

        notifyDataSetChanged()
    }
    /**
     * установка прогресса
     */
    private fun bindProgress(progress: Int, progressBar: ProgressBar) {
        progressBar.progressDrawable = when (progress) {
            in 0..90 -> ContextCompat.getDrawable(context, R.drawable.green_progress_bar_drawable)
            in 90..94 -> ContextCompat.getDrawable(context, R.drawable.yellow_progress_bar_drawable)
            else -> ContextCompat.getDrawable(context, R.drawable.red_progress_bar_drawable)
        }
        progressBar.progress = progress
    }
    /**
     * холдер оборудования
     */
    inner class EquipmentViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            item_locomotive_main_info.setOnClickListener {
                expandEquipment(adapterData.getEquipment(adapterPosition), adapterPosition)
            }
        }

        fun bind(item: EquipmentItem) {
            /**
             * привязка данных к элементам в ячейке
             */
            val equipment = item.equipment
            /**
             * отрисовка иконки в зависимости от того, развернут ли локомотив
             */
            if (item.isExpanded) {
                locomotive_arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_drop_up))
            } else {
                locomotive_arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_drop_down))
            }

            equipment_works_counts.text = WorksCountStringBuilder.build(equipment.worksCount, context)
            bindProgress(equipment.progress, locomotive_detail_progress_bar)

            equipment.timeLeft = Date(equipment.timeLeft.time - timeDelta)
            val rawRemainingTime = equipment.timeLeft.time
            locomotive_time.text = DateUtils.convertToString(rawRemainingTime)
            /**
             * раскраска времени в зависимости от того сколько времени осталось
             */
            if (rawRemainingTime >= 0L) {
                locomotive_time.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
            } else {
                locomotive_time.setTextColor(ContextCompat.getColor(context, R.color.appRed))
            }
            /**
             * установка данных локомотива и процентов
             */
            locomotive_title.text = context.getString(R.string.locomotive_activity_locomotive_title, equipment.number, equipment.subNumber)
            locomotive_percent.text = context.getString(R.string.locomotive_activity_percent, equipment.progress)
        }
    }
    /**
     * холдер линейного оборудования
     */
    inner class LineEquipmentViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            line_equipment_main_info.setOnClickListener {
                lineEquipmentClickListener?.invoke(adapterData.getLineEquipment(adapterPosition).equipment)
            }
        }
        /**
         * привязка данных к элементам в ячейке
         */
        fun bind(item: LineEquipmentItem) {
            val equipment = item.equipment
            /**
             * изменение цвета у выбранного оборудования
             */
            if (equipment.isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedEquipment))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.appWhite))
            }

            equipment.timeLeft = Date(equipment.timeLeft.time - timeDelta)
            val rawRemainingTime = equipment.timeLeft.time
            line_equipment_time.text = DateUtils.convertToString(rawRemainingTime)
            /**
             * раскраска времени в зависимости от оставшегося времени
             */
            if (rawRemainingTime >= 0L) {
                line_equipment_time.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
            } else {
                line_equipment_time.setTextColor(ContextCompat.getColor(context, R.color.appRed))
            }
            /**
             * установка данных линейного оборудования
             */
            bindProgress(equipment.progress, line_equipment_progress_bar)
            line_equipment_percent.setText(context.getResources().getString(R.string.locomotive_activity_percent, equipment.progress))

            line_equipment_title.text = context.getString(R.string.locomotive_activity_locomotive_title, equipment.name, equipment.number)
        }
    }
    /**
     * холдер секции
     */
    inner class SectionViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            item_section.setOnClickListener {
                val item = adapterData.getSectionData(adapterPosition)
                sectionClickListner?.invoke(item.section)
            }
        }

        fun bind(item: SectionItem) {
            val section = item.section
            /**
             * раскраска выбранной секции
             */
            if (section.isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedEquipment))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.appWhite))
            }
            /**
             * раскраска точки в зависимости от состояния секции
             */
            section_view_dot.background = when (section.state) {
                SectionState.WAITING -> context.getDrawable(R.drawable.yellow_dot)
                SectionState.IN_SERVICE -> context.getDrawable(R.drawable.green_dot)
                SectionState.UNDEFINED -> context.getDrawable(R.drawable.red_dot)
            }
            /**
             * установка данных секции
             */
            section_title.text = context.getString(R.string.locomotive_activity_section_title, section.name, section.number, section.subNumber)
            section_type.text = section.type
            /**
             * установка типа ремонта
             */
            section_repair_type.text = section.repairType?.let { context.getString(R.string.item_section, it.name) }
            section_works_count.text = WorksCountStringBuilder.build(section.worksCount, context)
            /**
             * установка иконок
             */
            section.equipmentHealth.forEach { equipmentHealth ->
                val equipmentHealthView = when (equipmentHealth.position) {
                    1 -> first_health_icon
                    2 -> second_health_icon
                    3 -> third_health_icon
                    else -> fourth_health_icon
                }
                /**
                 * установка цвета иконки
                 */
                equipmentHealthView.setBackgroundColor(when (equipmentHealth.status) {
                    EquipmentHealthStatus.GREEN -> ContextCompat.getColor(context, R.color.appGreen)
                    EquipmentHealthStatus.YELLOW -> ContextCompat.getColor(context, R.color.appYellow)
                    else -> ContextCompat.getColor(context, R.color.appRed)
                })
                /**
                 * установка картинки иконки
                 */
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
     * холдер разделителя
     */
    inner class DividerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_EQUIPMENT = 0
        private const val VIEW_TYPE_LINE_EQUIPMENT = 1
        private const val VIEW_TYPE_SECTION = 2
        private const val VIEW_TYPE_DIVIDER = 3
    }
}