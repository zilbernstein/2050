package ru.digipeople.locotech.master.ui.activity.tmcsearch.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.TMC
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcSearchAdapter @Inject constructor() : BaseItemsRecyclerAdapter<TMC, TmcSearchAdapter.ViewHolder>() {

    var onItemClickListener: ((tmc: TMC) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.card_search_tmc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position)!!)
    }
    /**
     * Холдер ТМЦ
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tmc_title)
        private val sectionLeft: TextView = view.findViewById(R.id.section_left)
        private val stockLeft: TextView = view.findViewById(R.id.stock_left)
        private val uom: TextView = view.findViewById(R.id.uom_value)

        init {
            /**
             * Обработка нажатия на элемент списка
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
        fun bind(tmc: TMC) {
            title.text = tmc.name
            sectionLeft.text = "${tmc.workshop}"
            stockLeft.text = "${tmc.stockRest}"
            uom.text = tmc.uom
        }
    }
}