package ru.digipeople.locotech.inspector.ui.activity.remarksearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.RemarkFromCatalog
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер добавления/выбора замечаний
 * @author Kashonkov Nikita
 */
class RemarkSearchAdapter @Inject constructor() :
        BaseItemsRecyclerAdapter<RemarkFromCatalog, RemarkSearchAdapter.RemarkViewHolder>() {

    //region Other
    var itemClickListener: ((remark: RemarkFromCatalog) -> Unit)? = null
    //endregion
    /**
     * создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_remark_search, parent, false)
        return RemarkViewHolder(view)
    }
    /**
     * привязка данных к шаблону
     */
    override fun onBindViewHolder(vh: RemarkViewHolder, position: Int) {
        vh.itemTitle.text = items[position].title
    }
    /**
     * Холдер замечания
     */
    inner class RemarkViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val itemTitle: TextView

        init {
            itemTitle = view.findViewById(R.id.item_remark_title)
            itemTitle.setOnClickListener { itemClickListener?.invoke(items[adapterPosition]) }
        }
    }
}