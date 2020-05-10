package ru.digipeople.locotech.worker.ui.activity.tmcshortage.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_tmc_shortage.view.*
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.TMCInWork
import javax.inject.Inject

/**
 * Адаптер для ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcShortageAdapter @Inject constructor(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<TmcShortageAdapter.TmcViewHolder>() {
    //region Other
    lateinit var dataSet: List<TMCInWork>
    lateinit var currentSet: List<TMCInWork>

    var itemClickListener: ((tmc: TMCInWork) -> Unit)? = null
    //endregion
    /**
     * Холдер ТМЦ
     */
    class TmcViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val title = view.card_tmc_title
        val icon = view.card_tmc_checked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TmcViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_tmc_shortage, parent, false)
        return TmcViewHolder(view)
    }
    /**
     * Определение количества элементов
     */
    override fun getItemCount(): Int {
        return dataSet.size
    }
    /**
     * Привязка данных к шаблону
     */
    override fun onBindViewHolder(p0: TmcViewHolder, p1: Int) {
        val tmc = dataSet.get(p1)

        p0.title.text = dataSet.get(p1).name

        if (currentSet.contains(tmc)) {
            p0.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_red))
        } else {
            p0.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.round_gray_frame))
        }

        p0.itemView.setOnClickListener { itemClickListener?.invoke(tmc) }
    }

}