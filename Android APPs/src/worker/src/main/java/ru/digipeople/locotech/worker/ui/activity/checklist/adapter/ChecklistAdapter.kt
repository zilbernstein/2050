package ru.digipeople.locotech.worker.ui.activity.checklist.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_checklist.view.*
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.ChecklistItem
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter

/**
 * Адаптер для чеклиста
 *
 * @author Sostavkin Grisha
 */
class ChecklistAdapter : BaseItemsRecyclerAdapter<ChecklistItem, ChecklistAdapter.ViewHolder>() {
    //region Other
    var onToggleListener: ((checklistItem: ChecklistItem) -> Unit)? = null
    //endregion
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_checklist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(items[position])
    }
    /**
     * Холдер чеклиста
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.name
        private val text: TextView = view.text
        private val checkBox: TextView = view.checkbox

        init {
            /**
             * обработка отметки чекбокса
             */
            checkBox.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onToggleListener?.invoke(it)
                }
            }
        }
        /**
         * Привязка данных к шаблонуа
         */
        fun bind(checklistItem: ChecklistItem) {
            name.text = checklistItem.name
            text.text = checklistItem.link
            checkBox.isSelected = checklistItem.isChecked
        }
    }
}