package ru.digipeople.ui.dialog.repairtype.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.ui.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.dialog.repairtype.RepairTypeViewModel

class RepairTypesAdapter : BaseItemsRecyclerAdapter<RepairTypeViewModel, RepairTypesAdapter.ViewHolder>() {
    val selectedItems = mutableSetOf<RepairTypeViewModel>()
    /**
     * Создание холдера
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
             * Обрабокта нажатия на элемент списка
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
         * Привязка данных
         */
        fun bind(item: RepairTypeViewModel) {
            title.text = item.name
            checkBox.isActivated = selectedItems.contains(item)
        }
    }
}