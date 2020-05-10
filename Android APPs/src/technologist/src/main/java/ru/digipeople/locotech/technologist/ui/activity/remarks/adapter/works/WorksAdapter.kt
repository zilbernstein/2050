package ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.works

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_remark_work.view.*
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.model.Work
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter

/**
 * Адаптер работ
 */
class WorksAdapter : BaseItemsRecyclerAdapter<Work, WorksAdapter.ViewHolder>() {

    var onApproveCheckedChangeListener: ((work: Work) -> Unit)? = null
    var onItemClickListener: ((work: Work) -> Unit)? = null
    private lateinit var recyclerView: RecyclerView
    private var selectedWorks = emptyMap<String, Work>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_remark_work, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }
    /**
     * Обновление состояния
     */
    fun updateSelectedState(selectedWorks: Map<String, Work>) {
        this.selectedWorks = selectedWorks
        notifyDataSetChanged()
    }
    /**
     * Холдер работы
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: TextView = itemView.checkbox
        private val count: TextView = itemView.repeats_count
        private val workName: TextView = itemView.remark_work_name

        init {
            /**
             * Обработка нажатия на работу
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener?.invoke(it)
                }
            }
            /**
             * Обрабока установки значения в чекбоксе
             */
            checkBox.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onApproveCheckedChangeListener?.invoke(it)
                }
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(work: Work) {
            workName.text = work.name
            count.text = work.repeats.toString()
            checkBox.isSelected = selectedWorks.containsKey(work.id)
        }
    }
}