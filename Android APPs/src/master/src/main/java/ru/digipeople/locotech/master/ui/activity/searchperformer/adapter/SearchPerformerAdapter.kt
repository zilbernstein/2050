package ru.digipeople.locotech.master.ui.activity.searchperformer.adapter

import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.PerformerItem
import kotlinx.android.synthetic.main.item_performer_search.view.*
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.dagger.ScreenScope
import javax.inject.Inject

/**
 * Адаптер выбора сотрудника / исполнителя
 * @author Kashonkov Nikita
 */
@ScreenScope
class SearchPerformerAdapter @Inject constructor() :
        BaseItemsRecyclerAdapter<PerformerItem, SearchPerformerAdapter.ViewHolder>() {
    var itemClickListener: ((performer: PerformerItem) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_performer_search, parent, false)
        return ViewHolder(view)
    }
    /**
     * Привязка данных к шаблону
     */
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val performer = items[position]
        vh.itemName.text = performer.performer.name
        vh.itemPercent.text = context.getString(R.string.search_performer_percent, performer.loadPercent.toInt())
        vh.loadPercent.setProgress(performer.loadPercent.toInt())
        /**
         * Раскраска загрузки исполнителя
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
     * Холдер
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val itemName: AppCompatTextView = view.item_performer_name
        val itemPercent: AppCompatTextView = view.item_performer_percent
        val loadPercent: ProgressBar = view.progress

        init {
            /**
             * Обработка нажатия на элемент списка
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    itemClickListener?.invoke(it)
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }
}