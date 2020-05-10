package ru.digipeople.message.ui.activity.chooseaddressee.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.message.R
import ru.digipeople.message.model.Contact
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter

/**
 * Адаптер для выбора адресата 
 */
class ChooseAddresseeAdapter : BaseItemsRecyclerAdapter<Contact, ChooseAddresseeAdapter.ViewHolder>() {
    /**
     * Обработа нажатия на элемент списка
     */
    var onItemClickListener: ((contact: Contact) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_choose_addresse, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val contact = items[position]
        vh.bind(contact)
    }
    /**
     * Холдер контакта
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val itemName: TextView = view.findViewById(R.id.item_addressee_name)
        private val icon: ImageView = view.findViewById(R.id.item_addressee_checked)

        init {
            /**
             * Обработка выбора контакта
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener?.invoke(it)
                    notifyItemChanged(adapterPosition)
                }
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(contact: Contact) {
            itemName.text = contact.name
            icon.isSelected = contact.isSelected
        }
    }
}