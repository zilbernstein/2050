package ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер причин удаления замечания
 * @author Kashonkov Nikita
 */
class DeclinedReasonAdapter @Inject constructor() : BaseItemsRecyclerAdapter<DeclinedReasonModel, DeclinedReasonAdapter.ViewHolder>() {
    /**
     * Обработка нажатия на причину удаления
     */
    var onReasonClickListener: ((reasonModel: DeclinedReasonModel) -> Unit)? = null
    /**
     * Создангие холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_decline_reason, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position)!!)
    }
    /**
     * Холдер причин
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val reason: TextView = view.findViewById(R.id.reason)
        init {
            itemView.setOnClickListener {
                onReasonClickListener?.invoke(items.get(adapterPosition))
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(reasonModel: DeclinedReasonModel) {
            reason.text = reasonModel.name
        }
    }
}