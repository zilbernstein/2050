package ru.digipeople.locotech.worker.ui.activity.choosereason.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_reason_choose.view.*
import ru.digipeople.ui.adapter.BaseDataAdapter
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.PauseReason
import javax.inject.Inject

/**
 * Адаптер для структуры выбора причины
 *
 * @author Kashonkov Nikita
 */
class ReasonAdapter @Inject constructor(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder>(), BaseDataAdapter<PauseReason> {

    //region Other
    var dataList: List<PauseReason> = emptyList()

    var itemClickListener: ((reason: PauseReason) -> Unit)? = null
    //endregion
    /**
     * Холдер причины
     */
    inner class ReasonViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val title = view.card_reason_title

        init {
            /**
             * Обработка нажатия на элемент
             */
            itemView.setOnClickListener { itemClickListener?.invoke(dataList.get(adapterPosition)) }
        }
    }
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_reason_choose, parent, false)
        return ReasonViewHolder(view)
    }
    /**
     * Определение количества элементов
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: ReasonViewHolder, p1: Int) {
        p0.title.text = dataList.get(p1).reasonName
    }
    /**
     * Установка данных
     */
    override fun setData(list: List<PauseReason>) {
        val diffUtilCallback = DiffUtilCallback(dataList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataList = list
        diffResult.dispatchUpdatesTo(this)
    }
}