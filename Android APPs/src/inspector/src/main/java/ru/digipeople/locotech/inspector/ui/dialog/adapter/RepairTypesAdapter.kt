package ru.digipeople.locotech.inspector.ui.dialog.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.ui.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.locotech.inspector.ui.dialog.RepairTypeViewModel

class RepairTypesAdapter : BaseItemsRecyclerAdapter<RepairTypeViewModel, RepairTypesAdapter.ViewHolder>() {
    val selectedItems = mutableSetOf<RepairTypeViewModel>()
    /**
     * создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_repair_type, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    /**
     * Холдер типа ремонта
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.repair_type_text)
        private val checkBox = itemView.findViewById<ImageView>(R.id.repair_type_check)

        init {
            /**
             * обработка выбора типа ремонта
             */
            this.itemView.setOnClickListener {
                val item = items[adapterPosition]
                val itemIsSelected = selectedItems.contains(item)
                if (itemIsSelected) {
                    selectedItems.remove(item)
                } else selectedItems.add(item)
                notifyItemChanged(adapterPosition)
            }
        }
        /**
         * привязка данных к шаблону
         */
        fun bind(item: RepairTypeViewModel) {
            title.text = item.name
            checkBox.isActivated = selectedItems.contains(item)
        }
    }
}