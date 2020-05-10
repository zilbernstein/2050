package ru.digipeople.locotech.inspector.ui.activity.summary.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер суммарной информации
 *
 * @author Sostavkin Grisha
 */
class SummaryAdapter @Inject constructor() :
        BaseItemsRecyclerAdapter<SummaryItem, SummaryAdapter.ViewHolder>() {
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_summary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position)!!)
    }
    /**
     * Холдер суммарной информации
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val itemName: TextView = view.findViewById(R.id.item_title)
        private val progress: ProgressBar = view.findViewById(R.id.cyclic_progress)
        private val progressText: TextView = view.findViewById(R.id.cyclic_progress_text)
        private val accepted: TextView = view.findViewById(R.id.cyclic_accepted)
        private val declinedOTK: TextView = view.findViewById(R.id.cyclic_declined)
        private val declinedRJD: TextView = view.findViewById(R.id.cyclic_declined_rjd)
        /**
         * привязка данных к шаблону
         */
        fun bind(summaryItem: SummaryItem) {
            itemName.text = summaryItem.name
            progress.progress = summaryItem.percent
            progressText.text = context.getString(R.string.summary_activity_percent, summaryItem.percent)
            declinedRJD.text = summaryItem.declinedRJD.toString()
            accepted.text = summaryItem.accepted.toString()
            declinedOTK.text = summaryItem.declinedOTK.toString()
        }
    }
}