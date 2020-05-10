package ru.digipeople.locotech.master.ui.activity.assignmenttemplates.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_assignment_template.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.adapter.item.AssignmentTemplateItem
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
/**
 * Адаптер выбора шаблона исполнителей
 */
class AssignmentTemplatesAdapter : BaseItemsRecyclerAdapter<AssignmentTemplateItem, AssignmentTemplatesAdapter.ViewHolder>() {
    var onItemClickListener: ((item: AssignmentTemplateItem) -> Unit)? = null
    /**
     * Операции при создании экрана
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_assignment_template, parent, false))
    }
    /**
     * Операции при прикреплении экрана
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    /**
     * Холдер шаблона исполнителей
     */
    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            val item by lazy { items[adapterPosition] }
            /**
             * Обработчик нажатия на элемент списка шаблонов
             */
            itemView.setOnClickListener { onItemClickListener?.invoke(item) }
        }

        fun bind(item: AssignmentTemplateItem) {
            /**
             * инициализация значений списка
             */
            assignment_template_name.text = item.name
        }
    }
}