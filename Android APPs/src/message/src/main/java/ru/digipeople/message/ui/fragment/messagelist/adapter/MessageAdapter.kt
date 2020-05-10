package ru.digipeople.message.ui.fragment.messagelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.digipeople.message.R
import ru.digipeople.message.model.Message
import ru.digipeople.message.model.MessageStatus
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.dagger.ScreenScope
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Адаптер для фрагмента списка сообщения
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
class MessageAdapter @Inject constructor() : BaseItemsRecyclerAdapter<Message, MessageAdapter.ViewHolder>() {
    /**
     * Действия при нажатии на сообщение
     */
    var onItemClickListener: ((message: Message) -> Unit)? = null
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val message = items[position]
        vh.bind(message)
    }
    /**
     * Холдер сообщения
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val contact: TextView = view.findViewById(R.id.item_message_contact)
        val date: TextView = view.findViewById(R.id.item_message_date)
        val status: TextView = view.findViewById(R.id.item_message_status)
        val statusView: View = view.findViewById(R.id.item_message_status_view)

        init {
            /**
             * обработка нажатия на сообщение
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
        fun bind(message: Message) {
            if (!message.isIncoming) {
                if (message.recipients.isNotEmpty()) {
                    val contacts = StringBuilder()

                    for (name in message.recipients) {
                        contacts.append(name.name)
                        contacts.append(", ")
                    }

                    contacts.delete(contacts.length - 2, contacts.length - 1)

                    contact.text = contacts.toString()
                }
            } else {
                if (message.sender != null) {
                    contact.text = message.sender.name
                }
            }

            date.text = message.date?.let { dateFormat.format(it) } ?: ""
            when (message.messageStatus) {
                /**
                 * раскраска в зависимости о статуса
                 */
                MessageStatus.UNKNOWN -> {
                    /**
                     * не удалось отправить
                     */
                    status.text = context.getString(R.string.message_activity_send_mistake)
                    status.setTextColor(ContextCompat.getColor(context, R.color.appRed))
                    statusView.background = ContextCompat.getDrawable(context, R.drawable.small_red_dot)
                }
                MessageStatus.READ -> {
                    /**
                     * прочтено
                     */
                    status.text = context.getString(R.string.message_activity_read)
                    status.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
                    statusView.background = ContextCompat.getDrawable(context, R.drawable.small_green_dot)
                }
                MessageStatus.SENT -> {
                    /**
                     * доставлено
                     */
                    status.text = context.getString(R.string.message_activity_delivered)
                    status.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
                    statusView.background = ContextCompat.getDrawable(context, R.drawable.small_green_dot)
                }
            }
        }
    }
}