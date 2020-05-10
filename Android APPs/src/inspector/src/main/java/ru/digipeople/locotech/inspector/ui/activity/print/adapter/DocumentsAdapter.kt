package ru.digipeople.locotech.inspector.ui.activity.print.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер документов
 *
 * @author Kashonkov Nikita
 */
class DocumentsAdapter @Inject constructor() : BaseItemsRecyclerAdapter<DocumentModel, DocumentsAdapter.ViewHolder>() {

    var onItemClickListener: ((document: DocumentModel) -> Unit)? = null
    /**
     * создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_document_for_print, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position)!!)
    }
    /**
     * Холдер документа
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.document_title)
        private val stateImage: ImageView = view.findViewById(R.id.check_state)

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
        fun bind(document: DocumentModel) {
            title.text = document.name
            stateImage.isActivated = document.isSelected
        }
    }
}