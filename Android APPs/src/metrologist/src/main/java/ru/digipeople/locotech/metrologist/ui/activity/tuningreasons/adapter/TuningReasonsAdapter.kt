package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Reason
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter

/**
 * Адаптер выбора причины обточки
 * @author Michael Solenov
 */
class TuningReasonsAdapter : BaseItemsRecyclerAdapter<Reason, TuningReasonsAdapter.ViewHolder>() {
    var onItemClickListener: ((reason: Reason) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_turning_reason, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val reason = items[position]
        vh.bind(reason)
    }
    /**
     * Холдер причины обточки
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)

        init {
            /**
             * Обработка нажатия на причину обточки
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener?.invoke(it)
                }
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(reason: Reason) {
            title.text = reason.name
        }
    }
}