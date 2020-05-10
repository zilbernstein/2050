package ru.digipeople.locotech.master.ui.dialog.selectequipment.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.master.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
/**
 * Адаптер диалога выбора оборудования из бокового меню.
 */
class EquipmentsAdapter : BaseItemsRecyclerAdapter<ShortEquipment, EquipmentsAdapter.ViewHolder>() {

    var selectedEquipmentId: String? = null
    var onItemClickListener: ((ShortEquipment) -> Unit)? = null
    /**
     * Действи япри создании окна
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_select_equipment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(items[position])
    }
    /**
     * Холдер
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameView = itemView as TextView

        init {
            /**
             * Обработка нажатия на элемент
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
        fun bind(equipment: ShortEquipment) {
            nameView.text = equipment.name
            itemView.isSelected = equipment.id == selectedEquipmentId
        }
    }
}