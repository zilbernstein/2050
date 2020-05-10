package ru.digipeople.message.ui.activity.writemessage.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.message.R
import ru.digipeople.message.model.Contact
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter

/**
 * Адаптер для адресатов
 */
class AddresseeAdapter : BaseItemsRecyclerAdapter<Contact, AddresseeAdapter.AddresseeViewHolder>() {
    /**
     * обработка удаления контакта
     */
    var onDeleteItemClickListener: ((contact: Contact) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddresseeViewHolder {
        val view = layoutInflater.inflate(R.layout.item_addressee_wrirte_message, parent, false)
        return AddresseeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddresseeViewHolder, position: Int) {
        val contact = items[position]
        holder.bind(contact)
    }
    /**
     * Холдер адресатов
     */
    inner class AddresseeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.item_addressee_write_message_name)
        private val deleteBtn: View = view.findViewById(R.id.item_addressee_write_message_delete_btn)

        init {
            /**
             * Обработка удаления адресата
             */
            deleteBtn.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onDeleteItemClickListener?.invoke(it)
                }
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(contact: Contact) {
            name.text = contact.name
        }
    }
}


