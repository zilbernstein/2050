package ru.digipeople.locotech.inspector.ui.activity.repairbook.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.Document
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
/**
 * Адаптер книги ремонтов
 */
class DocumentsAdapter : BaseItemsRecyclerAdapter<Document, DocumentsAdapter.ViewHolder>() {

    var onItemClickListener: ((item: Document) -> Unit)? = null
    /**
     * Выбранный элемент
     */
    var selectedItemId: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val document = getItem(position)!!
        vh.bind(document)
    }
    /**
     * Холдер
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.page_title)

        init {
            /**
             * обработка нажатия на документ
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener?.invoke(it)
                }
            }
        }
        /**
         * привязка данных к шаблону
         */
        fun bind(document: Document) {
            title.text = document.name
            itemView.isSelected = document.id == selectedItemId
        }
    }
}
