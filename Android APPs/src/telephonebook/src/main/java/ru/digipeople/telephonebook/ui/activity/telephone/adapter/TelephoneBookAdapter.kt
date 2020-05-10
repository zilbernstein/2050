package ru.digipeople.telephonebook.ui.activity.telephone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.volcaniccoder.volxfastscroll.IVolxAdapter
import ru.digipeople.telephonebook.R
import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.dagger.ScreenScope
import javax.inject.Inject

/**
 * Адаптер для телефонного справочника
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
class TelephoneBookAdapter @Inject constructor() : BaseItemsRecyclerAdapter<Contact, TelephoneBookAdapter.ViewHolder>(), IVolxAdapter<Contact> {
    /**
     * обработка нажатия на контакт
     */
    var itemClickListener: ((contact: Contact) -> Unit)? = null
    /**
     * создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_telephone_book_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val contact = getItem(position)!!
        vh.bind(contact)
    }
    /**
     * получение списка контактов
     */
    override fun getList(): List<Contact> {
        return items
    }
    /**
     * Холдер контактов
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val contactName: TextView = view.findViewById(R.id.item_contact_name)
        private val contactNumberText: TextView = view.findViewById(R.id.item_contact_number)
        private val contactNumberImage: ImageButton = view.findViewById(R.id.item_call_button)

        init {
            /**
         * обработка нажатия на контакт
         */
            contactNumberImage.setOnClickListener {
                getItem(adapterPosition)?.let {
                    itemClickListener?.invoke(it)
                }
            }
        }
        /**
         * привязка данных к шаблону
         */
        fun bind(contact: Contact) {
            contactName.text = contact.name
            contactNumberText.text = contact.phoneNumber
        }
    }
}