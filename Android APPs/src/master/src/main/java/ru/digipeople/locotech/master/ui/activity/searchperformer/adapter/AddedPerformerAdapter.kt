package ru.digipeople.locotech.master.ui.activity.searchperformer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_performer_search.view.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.PerformerItem
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер добавления сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
class AddedPerformerAdapter @Inject constructor() : BaseItemsRecyclerAdapter<PerformerItem, AddedPerformerAdapter.ViewHolder>() {
    var onItemClickListener: ((performer: PerformerItem) -> Unit)? = null
    /**
     * Сощдание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_performer_added, parent, false)
        return ViewHolder(view)
    }
    /**
     * Привязка данных к шаблону
     */
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val performer = getItem(position)!!
        vh.itemName.text = performer.performer.name
        val newLoadPercent = Math.max(0.0, performer.newLoadPercent)
        vh.itemPercent.text = context.getString(R.string.search_performer_percent, newLoadPercent.toInt())
        vh.loadPercent.progress = newLoadPercent.toInt()
        /**
         * Раскраска прогресс бара
         */
        when (performer.loadPercent.toInt()) {
            in 0..100 -> {
                vh.loadPercent.progressDrawable = ContextCompat.getDrawable(context, R.drawable.performer_search_green_progress_bar)
            }
            in 100..149 -> {
                vh.loadPercent.progressDrawable = ContextCompat.getDrawable(context, R.drawable.performer_search_yellow_progress_bar)
            }
            else -> {
                vh.loadPercent.progressDrawable = ContextCompat.getDrawable(context, R.drawable.performer_search_red_progress_bar)
            }
        }
    }
    /**
     * Холдер исполнителя
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: AppCompatTextView = view.item_performer_name
        val itemPercent: AppCompatTextView = view.item_performer_percent
        val loadPercent: ProgressBar = view.progress

        init {
            /**
             * Обработка нажатия на исполнителя
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener?.invoke(it)
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }
}