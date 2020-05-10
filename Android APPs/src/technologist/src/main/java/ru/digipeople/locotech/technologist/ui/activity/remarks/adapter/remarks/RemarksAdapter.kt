package ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_remark_card.view.*
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.model.Remark
import ru.digipeople.ui.adapter.BaseRecyclerAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Адаптер замечаний
 */
class RemarksAdapter : BaseRecyclerAdapter<RecyclerView.ViewHolder>() {

    var adapterData = AdapterData()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var currentRemarkId: String? = null
    var onRemarkClickListener: ((remark: Remark) -> Unit)? = null
    /**
     * Создание холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        /**
         * Оборудование
         */
        VIEW_TYPE_EQUIPMENT -> {
            val view = layoutInflater.inflate(R.layout.item_remark_equipment, parent, false)
            EquipmentViewHolder(view)
        }
        /**
         * Замечание
         */
        VIEW_TYPE_REMARK -> {
            val view = layoutInflater.inflate(R.layout.item_remark_card, parent, false)
            RemarkViewHolder(view)
        }
        else -> throw IllegalArgumentException("Unknown view type $viewType")
    }
    /**
     * Определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        return if (adapterData.isEquipment(position)) {
            VIEW_TYPE_EQUIPMENT
        } else if (adapterData.isRemark(position)) {
            VIEW_TYPE_REMARK
        } else {
            throw IllegalArgumentException("Unknown item type at position $position")
        }
    }
    /**
     * Полученеи числа элементов
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (adapterData.isEquipment(position)) {
            val vh = holder as EquipmentViewHolder
            vh.bind(adapterData.getEquipment(position))
        } else if (adapterData.isRemark(position)) {
            val vh = holder as RemarkViewHolder
            vh.bind(adapterData.getRemark(position))
        }
    }

    fun toggleExpanded(equipmentModel: EquipmentModel, position: Int) {
        if (equipmentModel.isExpanded) {
            equipmentModel.isExpanded = false
            notifyItemChanged(position, Unit)
            adapterData.subList(position + 1, position + equipmentModel.remarks.size + 1).clear()
            notifyItemRangeRemoved(position + 1, equipmentModel.remarks.size)
        } else {
            equipmentModel.isExpanded = true
            notifyItemChanged(position, Unit)
            adapterData.addAll(position + 1, equipmentModel.remarks)
            notifyItemRangeInserted(position + 1, equipmentModel.remarks.size)
        }
    }
    /**
     * преобразование даты в строку
     */
    fun dateToString(date: Date?): String? {
        return if (date == null) null else dateFormat.format(date)
    }
    /**
     * Холдер замечания
     */
    inner class RemarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemSectionName = view.section_number
        private val itemTitleCardId = view.card_remark_id
        private val itemTitleCardDate = view.card_remark_data
        private val itemTitleCardWorker = view.card_remark_worker

        init {
            /**
             * Обрабокта нажатия на замечание
             */
            itemView.setOnClickListener {
                val remark = adapterData.getRemark(adapterPosition).remark
                currentRemarkId = remark.id
                notifyDataSetChanged()
                onRemarkClickListener?.invoke(remark)
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(remark: RemarkModel) {
            itemTitleCardId.text = remark.remark.name
            itemTitleCardDate.text = dateToString(remark.remark.date)
            itemTitleCardWorker.text = remark.remark.author
            itemSectionName.text = remark.section.id
            itemView.isSelected = remark.remark.id == currentRemarkId
        }
    }
    /**
     * Холдер оборудования
     */
    inner class EquipmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemTitleLoco: TextView = view.findViewById(R.id.remark_loco_title)
        private val expandableIcon: ImageView = view.findViewById(R.id.expandableIcon)

        init {
            /**
             * Обработка нажатия на оборудование
             */
            itemView.setOnClickListener {
                val equipment = adapterData.getEquipment(adapterPosition)
                toggleExpanded(equipment, adapterPosition)
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(equipment: EquipmentModel) {
            itemTitleLoco.text = equipment.equipment.name
            expandableIcon.isSelected = equipment.isExpanded
        }
    }

    companion object {
        private const val VIEW_TYPE_EQUIPMENT = 0
        private const val VIEW_TYPE_REMARK = 1
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
}